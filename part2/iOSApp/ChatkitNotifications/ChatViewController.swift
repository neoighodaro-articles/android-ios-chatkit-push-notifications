
import UIKit
import PusherChatkit

class ChatViewController : UITableViewController {
    
    var messagesList = [PCMultipartMessage]() {
        didSet {
            self.tableView.reloadData()
        }
    }
    
    var currentUser: PCCurrentUser?
    
    @IBAction func sendNewMessage(_ sender: Any) {
        
        let alertController = UIAlertController(title: "New message", message: "Enter a new message", preferredStyle: .alert)
        
        let confirmAction = UIAlertAction(title: "Send", style: .default) { (_) in
            
            let newMessage = alertController.textFields?[0].text
            self.currentUser!.sendSimpleMessage(roomID: (self.currentUser!.rooms[0].id), text: newMessage!) { message, error in
                guard error == nil else {
                    print("Error sending message")
                    return
                }
                print("Sent message successfully")
            }
            
        }
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { (_) in }
        
        alertController.addTextField { (textField) in
            textField.placeholder = "Enter message"
        }
        
        alertController.addAction(confirmAction)
        alertController.addAction(cancelAction)
        
        self.present(alertController, animated: true, completion: nil)
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messagesList.count
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let currentItem = messagesList[indexPath.row]
        let messageCell = tableView.dequeueReusableCell(withIdentifier: "messageCell") as! TableCell
        messageCell.setValues(message: currentItem)
        
        return messageCell
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        subscribeToRoom()
    }
    
    
    func subscribeToRoom() {
        
        currentUser!.subscribeToRoomMultipart(
            room: currentUser!.rooms[0],
            roomDelegate: self
        ) { error in
            guard error == nil else {
                print("Error subscribing to room: \(error!.localizedDescription)")
                return
            }
            print("Subscribed to room!")
            
        }
        
    }
    
}


extension ChatViewController: PCRoomDelegate {
    func onMultipartMessage(_ message: PCMultipartMessage) {
        self.messagesList.append(message)
        DispatchQueue.main.async {
            self.tableView.reloadData()
        }
    }
    
}
