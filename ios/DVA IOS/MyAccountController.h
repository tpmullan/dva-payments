//
//  MyAccountController.h
//  DVA IOS
//
//  Created by praetorian guard on 7/30/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyAccount.h"

@interface MyAccountController : UIViewController
<MyAccountDelegate>
{
    
}
@property (weak, nonatomic) IBOutlet UITextField *txtName;
@property (weak, nonatomic) IBOutlet UITextField *txtPassword;
@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
@property (weak, nonatomic) IBOutlet UIButton *buttonDelete;
@property (weak, nonatomic) IBOutlet UIButton *buttonUpdateAccount;
@property (strong, atomic) MyAccount *client;
- (IBAction)backgroundClick:(id)sender;

@end
