
import UIKit
import PusherChatkit

class TableCell : UITableViewCell {
    
    @IBOutlet weak var username: UILabel!
    @IBOutlet weak var messagelabel: UILabel!
    
    func setValues(message:PCMultipartMessage) {
        
        for part in message.parts {
            switch part.payload {
            case .inline(let payload):
                username.text = message.sender.displayName
                messagelabel.text = payload.content
                
            case .url(let payload):
                print("Received message with url: \(payload.url)")
            case .attachment(let payload):
                payload.url() { downloadUrl, error in }
            }
        }
        
    }
    
}
