//
//  LoginRequest.m
//  DVA IOS
//
//  Created by praetorian guard on 7/25/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "LoginRequest.h"
#import <AFNetworking/AFNetworking.h>
#import "AccountStorage.h"

@implementation LoginRequest
@synthesize delegate;

- (void) authenticateUser:(NSString *)username withPassword:(NSString *)password;
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    
    NSLog(@"URL %@",url);
    
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            username, @"session[email]",
                            password, @"session[password]"
                            ,nil];
    

    
    [httpClient postPath:@"/sessions.json" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
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
         [delegate finishwithStatus:YES withMessage:@"Networking Error"];
     }];

}




@end
