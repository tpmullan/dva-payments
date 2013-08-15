//
//  DVASignUpController.h
//  DVA
//
//  Created by Tom M. on 7/9/13.
//  Copyright (c) 2013 Tom M. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <JSONKit/JSONKit.h>
#import <Foundation/Foundation.h>
#import "SignUp.h"

@interface SignUpController : UIViewController
<SignUpDelegate>
{
    
}
@property (weak, nonatomic) IBOutlet UITextField *txtName;
@property (weak, nonatomic) IBOutlet UITextField * txtUsername;
@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
@property (strong, nonatomic) IBOutlet UITextField * txtPassword;
@property (strong, nonatomic) IBOutlet UITextField *txtPasswordConfirmation;
- (IBAction)backgroundClick:(id)sender;
- (IBAction)SignUpClicked:(id)sender;
- (void) alertStatus:(NSString *)msg withTitle:(NSString *) title;
- (void) onSuccessfulSignUp;
- (void) onUnsuccessfulSignUpWithMessage:(NSString *) message;
@end
