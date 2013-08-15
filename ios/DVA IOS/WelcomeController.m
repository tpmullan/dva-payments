	//
//  WelcomeController.m
//  DVA IOS
//
//  Created by praetorian guard on 7/25/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//


#import "WelcomeController.h"


@interface WelcomeController ()

@end

@implementation WelcomeController

@synthesize txtPassword;
@synthesize txtUsername;
@synthesize rembermeSwitch;

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
    NSString *url = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSLog(@"url before is %@", url);
    
    // Note: this will not work for boolean values as noted by bpapa below.
    // If you use booleans, you should use objectForKey above and check for null
    if(!url) {
        [self registerDefaultsFromSettingsBundle];
        url = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    }
    NSLog(@"url after is %@", url);
    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    if([prefs boolForKey:@"rememberme"])
    {
        [txtUsername setText:[prefs objectForKey:@"username"]];
        [txtPassword setText:[prefs objectForKey:@"password"]];
    }
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)loginClicked:(id)sender
{
    NSString *boolvalue = rembermeSwitch.isOn? @"yes":@"no";
    NSString *str = [NSString stringWithFormat:@"Username: %@\n Password %@\nRememberme: %@",txtUsername.text,txtPassword.text,boolvalue];
    NSLog(@"%@",str);
    if([self checkLogin:txtUsername.text withPassword:txtPassword.text withRememberMe:rembermeSwitch.isOn])
    {
       //display UI elements for network loading
    }
    else
    {
        [self alertStatus:@"Please Try Again" withTitle:@"Login Failed"];
    }
}
-(bool) checkLogin: (NSString *) username withPassword:(NSString *) password withRememberMe:(bool) rememberMe
{
    LoginRequest *client = [[LoginRequest alloc] init];
    client.delegate = self;
    
    [client authenticateUser:username withPassword:password];
    
    
    
    return YES;
    
}
-(void) finishwithStatus:(BOOL)status withMessage:(NSString *)message
{
    NSLog(@"status:%u\nMessage:%@",status,message);
    if(status == YES)
    {
        [self onSuccessfulLogin ];
    }
    else
    {
        [self onUnsuccessfulLoginWithMessage: message];
    }
}

-(void) onSuccessfulLogin
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    UITabBarController *obj = [storyboard instantiateViewControllerWithIdentifier:@"AccountStoryline"];
    self.navigationController.navigationBarHidden=YES;
    [self.navigationController pushViewController:obj animated:YES];
    
    if(rembermeSwitch.isOn)
    {
        NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
        [prefs setObject:txtUsername.text forKey:@"username"];
        [prefs setObject:txtPassword.text forKey:@"password"];
        [prefs setBool:rembermeSwitch.isOn forKey:@"rememberme"];
    }

    
    
}
-(void) onUnsuccessfulLoginWithMessage:(NSString *) message
{
    
    [self alertStatus:message withTitle:@"Login Failed"];
}

- (IBAction)backgroundClick:(id)sender {
    [txtPassword resignFirstResponder];
    [txtUsername resignFirstResponder];
}

- (IBAction)switchRememberMe:(id)sender
{
    
}
- (void) alertStatus:(NSString *)msg withTitle:(NSString *) title
{

    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title message:msg delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    [alertView show];
}

- (void)registerDefaultsFromSettingsBundle {
    NSString *settingsBundle = [[NSBundle mainBundle] pathForResource:@"Settings" ofType:@"bundle"];
    if(!settingsBundle) {
        NSLog(@"Could not find Settings.bundle");
        return;
    }
    
    NSDictionary *settings = [NSDictionary dictionaryWithContentsOfFile:[settingsBundle stringByAppendingPathComponent:@"Root.plist"]];
    NSArray *preferences = [settings objectForKey:@"PreferenceSpecifiers"];
    
    NSMutableDictionary *defaultsToRegister = [[NSMutableDictionary alloc] initWithCapacity:[preferences count]];
    for(NSDictionary *prefSpecification in preferences) {
        NSString *key = [prefSpecification objectForKey:@"Key"];
        if(key && [[prefSpecification allKeys] containsObject:@"DefaultValue"]) {
            [defaultsToRegister setObject:[prefSpecification objectForKey:@"DefaultValue"] forKey:key];
        }
    }
    
    [[NSUserDefaults standardUserDefaults] registerDefaults:defaultsToRegister];
}
@end
