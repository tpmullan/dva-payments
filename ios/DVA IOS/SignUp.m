//
//  SignUp.m
//  DVA IOS
//
//  Created by Tom M. on 8/14/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "SignUp.h"

@implementation SignUp
@synthesize delegate;

- (void) signUpUser:(NSString *)username withName:(NSString *)name withPassword:(NSString *)password withConfirmation:(NSString *)confirmation withEmail:(NSString *)email
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    
    NSLog(@"URL %@",url);
    
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            username, @"user[username]",
                            name, @"user[name]",
                            email, @"user[email]",
                            confirmation, @"user[password_confirmation]",
                            password, @"user[password]"
                            ,nil];
    
    
    
    [httpClient postPath:@"/users.json" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
         
         NSArray *cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookiesForURL: url];
         for (NSHTTPCookie *cookie in cookies)
         {
             NSLog(@"%@",[NSHTTPCookieStorage sharedHTTPCookieStorage]);
         }
         
         NSLog(@"Request Successful, response '%@'",arr);
         
         if([[arr valueForKey:@"status"] boolValue] == YES)
         {
             NSArray *messageArray = [arr valueForKey:@"message"];
             NSLog(@"Backup Value: %@",messageArray);
             
             
             NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
             [prefs setObject:[arr valueForKey:@"userId"] forKey:@"userId"];
             
             
             [delegate finishwithStatus:YES withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
             
             
         }
         else
         {
             [delegate finishwithStatus:NO withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
             
         }
         
     } failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         NSLog(@"[HTTPClient Error]: %@",error.localizedDescription);
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:[[error localizedRecoverySuggestion] dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];
         
         if(arr == nil)
         {
             [delegate finishwithStatus:NO withMessage:@"Networking Error"];
         } else if ([[arr valueForKey:@"status"] boolValue] == NO)
         {
             [delegate finishwithStatus:NO withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
         }
     }];
}
@end
