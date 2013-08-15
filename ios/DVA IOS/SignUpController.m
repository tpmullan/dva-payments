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
- (void) alertStatus:(NSString *)msg :(NSString *) title
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

@end