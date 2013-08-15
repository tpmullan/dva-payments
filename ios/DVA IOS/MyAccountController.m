//
//  MyAccountController.m
//  DVA IOS
//
//  Created by praetorian guard on 7/30/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "MyAccountController.h"

@interface MyAccountController ()

@end

@implementation MyAccountController
@synthesize txtEmail;
@synthesize txtName;
@synthesize txtPassword;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    [self updateAccount];
    
    
}
-(void)updateAccountWithEmail:(NSString *) email withName:(NSString *) name withPassword:(NSString *) password
{
    MyAccount * client = [[MyAccount alloc] init];
    client.delegate = self;
    [client updateUserwithEmail:email withUsername:name withPassword:password];
    
}
-(void) finishWithStatus:(BOOL)status withMessage:(NSString *)message
{
   

[self alertStatus:message withTitle:@"Status"];
    
}
- (void) alertStatus:(NSString *)msg withTitle:(NSString *) title
{
    
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title message:msg delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    [alertView show];
}
-(void)updateAccount
{
    
    MyAccount * client = [[MyAccount alloc] init];
    client.delegate = self;
    [client getAccounts];
    
}
-(void) finishWithUserProfile:(NSArray *)response
{
    [txtEmail setText:[response valueForKey:@"email"]];
    [txtName setText:[response valueForKey:@"name"]];
    [txtPassword setText:@"changeMe"];
}
- (IBAction)deleteUserAction:(id)sender
{
    [self deleteUser];
}

-(void) deleteUser
{
    MyAccount * client = [[MyAccount alloc] init];
    client.delegate = self;
    [client deleteUser];
    
}
-(void) finishWithStatusAndExit:(BOOL)status
{
    if(status == YES)
    {
 //       UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
   //     UITabBarController *obj = [storyboard instantiateViewControllerWithIdentifier:@"LoginStoryline"];
     //   self.navigationController.navigationBarHidden=YES;
       // [self.navigationController pushViewController:obj animated:YES];
        
        [self.navigationController popToRootViewControllerAnimated:YES];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)updateAccountPressed:(id)sender
{
    [self updateAccountWithEmail:txtEmail.text withName:txtName.text withPassword:txtPassword.text];
}
- (IBAction)backgroundClick:(id)sender {
    [txtPassword resignFirstResponder];
    [txtName resignFirstResponder];
    [txtEmail resignFirstResponder];
}
@end
