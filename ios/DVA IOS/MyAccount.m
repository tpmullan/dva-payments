//
//  MyAccount.m
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "MyAccount.h"
#import <AFNetworking/AFNetworking.h>
#import "AccountStorage.h"

@implementation MyAccount
@synthesize delegate;

- (void) getBalance
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    
    [httpClient getPath:@"/balance.json" parameters:nil success:^(AFHTTPRequestOperation * operation, id responseObject)
     {
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options: NSJSONReadingMutableContainers error:nil];
         
         NSMutableArray *returnarray = [[NSMutableArray alloc] init];
         
                 
         NSLog(@"%@",arr);
         NSArray * accounts = [arr valueForKey:@"accounts"];
         for(int i = 0; i < [accounts count]; i++)
         {
             NSArray * singleAccount = [accounts objectAtIndex:i];
             Account * account = [[Account alloc] init];
             
             account.name = [singleAccount valueForKey:@"name"];
             account.balance = [NSNumber numberWithFloat:[[singleAccount valueForKey:@"balance"] floatValue]];
             account.number = [singleAccount valueForKey:@"number"];
             account.iid = [NSNumber numberWithInt:[[singleAccount valueForKey:@"id"] integerValue]];
             
             [AccountStorage addAccount:account];
             
             [returnarray addObject:account];
             
         }
         
         
         
         [delegate finishWithBalance:returnarray];
         
     } failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         NSLog(@"ERRORS EVERYWHERE\n%@",error);
         NSArray *cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookiesForURL: url];
         for (NSHTTPCookie *cookie in cookies)
         {
             NSLog(@"%@",[NSHTTPCookieStorage sharedHTTPCookieStorage]);
         }

     }];
}
- (void) updateUserwithEmail:(NSString *)email withUsername:(NSString *)username withPassword:(NSString *)password
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            email,@"",
                            username,@"",
                            password,@"",
                            nil];
    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    NSString * value = [prefs stringForKey:@"userId"];
    

 
    NSString * path = [NSString stringWithFormat:@"/users/%@.json",value];
    [httpClient putPath:path parameters:params success:^(AFHTTPRequestOperation * operation, id responseObject)
     {
         
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
         
         NSLog(@"%@",arr);
         
         if([[arr valueForKey:@"status" ] boolValue] == YES)
         {
             [delegate finishWithStatus:YES withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
         }
             
     } failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         NSLog(@"%@",error);
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:[[error localizedRecoverySuggestion] dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];
         
         
         NSLog(@"%@",arr);
         
        //this is breaking the protocal, we need to fix this so it is an array of messages.
         NSString * message = [arr valueForKey:@"message"];
         NSLog(@"%@",message);
        [delegate finishWithStatus:NO withMessage:message];
         
     }];
}
- (void) getAccounts
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    

    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    NSString * value = [prefs stringForKey:@"userId"];
    
    
    
    NSString * path = [NSString stringWithFormat:@"/users/%@.json",value];
    [httpClient getPath:path parameters:nil success:^(AFHTTPRequestOperation * operation, id responseObject)
     {
         
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
         
         NSLog(@"%@",arr);
         
         [delegate finishWithUserProfile:arr];
         
         
     } failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:[[error localizedRecoverySuggestion] dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];

         
         [delegate finishWithStatus:NO withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
         
     }];
}
- (void) deleteUser
{
    
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    
    
    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    NSString * value = [prefs stringForKey:@"userId"];
    
    
    
    NSString * path = [NSString stringWithFormat:@"/users/%@.json",value];
    [httpClient deletePath:path parameters:nil success:^(AFHTTPRequestOperation * operation, id responseObject)
     {
         
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
         
         NSLog(@"%@",arr);
         
         [delegate finishWithStatusAndExit:YES];
         
         
     } failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:[[error localizedRecoverySuggestion] dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];
         
         
         [delegate finishWithStatus:NO withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
         
     }];

}
@end
