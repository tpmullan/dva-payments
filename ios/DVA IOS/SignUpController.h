//
//  DVASignUpController.h
//  DVA
//
//  Created by Tom M. on 7/9/13.
//  Copyright (c) 2013 Tom M. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SignUpController : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *txtName;
@property (weak, nonatomic) IBOutlet UITextField * txtUsername;
@property (weak, nonatomic) IBOutlet UITextField *txtEmail;
@property (strong, nonatomic) IBOutlet UITextField * txtPassword;
@property (strong, nonatomic) IBOutlet UITextField *txtPasswordConfirmation;
- (IBAction)backgroundClick:(id)sender;
- (void) alertStatus:(NSString *)msg :(NSString *) title;
@end
