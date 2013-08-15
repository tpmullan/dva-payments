//
//  DVASignUpController.m
//  DVA
//
//  Created by Tom M. on 7/9/13.
//  Copyright (c) 2013 Tom M. All rights reserved.
//

#import "SignUpController.h"

@interface SignUpController ()

@end

@implementation SignUpController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)viewDidUnload
{
    [self setTxtPassword:nil];
    [self setTxtPasswordConfirmation:nil];
    [self setTxtUsername:nil];
    [self setTxtEmail:nil];
    [self setTxtName:nil];
    [super viewDidUnload];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void) alertStatus:(NSString *)msg withTitle:(NSString *) title
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title message:msg delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    [alertView show];
}

- (IBAction)backgroundClick:(id)sender {
    [_txtPassword resignFirstResponder];
    [_txtPasswordConfirmation resignFirstResponder];
    [_txtUsername resignFirstResponder];
    [_txtName resignFirstResponder];
    [_txtEmail resignFirstResponder];
}

- (IBAction)SignUpClicked:(id)sender {
    if([self trySignUp])
    {
        //display UI elements for network loading
    }
    else
    {
        [self alertStatus:@"Please Try Again" withTitle:@"Login Failed"];
    }

}

-(bool) trySignUp
{
    SignUp *client = [[SignUp alloc] init];
    client.delegate = self;
    
    [client signUpUser:_txtUsername.text withName:_txtName.text withPassword:_txtPassword.text withConfirmation:_txtPasswordConfirmation.text withEmail:_txtEmail.text ];
    
    return YES;
}
-(void) finishwithStatus:(BOOL)status withMessage:(NSString *)message
{
    NSLog(@"status:%u\nMessage:%@",status,message);
    if(status == YES)
    {
        [self onSuccessfulSignUp ];
    }
    else
    {
        [self onUnsuccessfulSignUpWithMessage: message];
    }
}

-(void) onSuccessfulSignUp
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    UITabBarController *obj = [storyboard instantiateViewControllerWithIdentifier:@"AccountStoryline"];
    self.navigationController.navigationBarHidden=YES;
    [self.navigationController pushViewController:obj animated:YES];
    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    [prefs setObject:_txtUsername.text forKey:@"username"];
    [prefs setObject:_txtPassword.text forKey:@"password"];
    [prefs setBool:YES forKey:@"rememberme"];
    
}

-(void) onUnsuccessfulSignUpWithMessage:(NSString *) message
{
    
    [self alertStatus:message withTitle:@"Sign Up Failed"];
}
@end