
import UIKit
import PusherChatkit

class LoginViewController: UIViewController {
    
    var chatManager: ChatManager!
    var currentUser: PCCurrentUser?
    var chatManagerDelegate: PCChatManagerDelegate?
    
    @IBOutlet weak var textField: UITextField!
    
    @IBAction func login(_ sender: Any) {
        setupChatManager()
    }
    
    func setupChatManager() {
        self.chatManager = ChatManager(
            instanceLocator: "v1:us1:3bff3c2b-c602-4626-9da9-66e758fb238a",
            tokenProvider: PCTokenProvider(url: "http://localhost:3000/token"),
            userID: self.textField.text!
        )
        
        self.chatManagerDelegate = MyChatManagerDelegate()
        
        self.chatManager.connect(
            delegate: self.chatManagerDelegate!
        ) { [unowned self] currentUser, error in
            guard error == nil else {
                print("Error connecting: \(error!.localizedDescription)")
                return
            }
            
            guard let currentUser = currentUser else {
                print("CurrentUser object is nil")
                return
            }
            
            currentUser.enablePushNotifications()
            self.currentUser = currentUser
            
            DispatchQueue.main.async() {
                let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                let newViewController = storyBoard.instantiateViewController(withIdentifier:"chatViewController")
                    as! ChatViewController
                
                newViewController.currentUser = currentUser
                
                let navigationController = UINavigationController(rootViewController: newViewController)
                self.present(navigationController, animated: true, completion: nil)
                
            }
            
        }
    }
    
}

class MyChatManagerDelegate: PCChatManagerDelegate {}
