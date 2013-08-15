//
//  WelcomeController.h
//  DVA IOS
//
//  Created by praetorian guard on 7/25/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <JSONKit/JSONKit.h>
#import <AFNetworking/AFNetworking.h>
#import "LoginRequest.h"

@interface WelcomeController : UIViewController
<LoginRequestDelegate>
{
    
}
@property (weak, nonatomic) IBOutlet UITextField *txtUsername;
@property (weak, nonatomic) IBOutlet UITextField *txtPassword;
@property (weak, nonatomic) IBOutlet UISwitch *rembermeSwitch;
- (IBAction)loginClicked:(id)sender;
- (IBAction)backgroundClick:(id)sender;
- (IBAction)switchRememberMe:(id)sender;
- (void) alertStatus:(NSString *)msg withTitle:(NSString *) title;
- (void) onSuccessfulLogin;
- (void) onUnsuccessfulWithMessage:(NSString *) message;
-(bool) checkLogin: (NSString *) username withPassword:(NSString *) password withRememberMe:(bool) rememberMe;
- (void)registerDefaultsFromSettingsBundle;

@end
