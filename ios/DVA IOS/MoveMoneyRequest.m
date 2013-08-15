//
//  MoveMoneyRequest.m
//  DVA IOS
//
//  Created by praetorian guard on 7/29/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "MoveMoneyRequest.h"
#import <AFNetworking/AFNetworking.h>

@implementation MoveMoneyRequest
@synthesize delegate;

-(void) sendTransferwithToAccount:(NSString *)toAccount withFromAccount:(NSString *)fromAccount withAmount:(NSNumber *)amount
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL: url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    

    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            amount,@"transfer[amount]",
                            toAccount,@"transfer[toAccount]",
                            fromAccount,@"transfer[fromAccount]", nil];
    
    [httpClient postPath:@"/transfer.json" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        
        if([[arr valueForKey:@"status"] boolValue] == YES)
        {

            [delegate finishWithStatus:YES withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"%@",[error localizedRecoverySuggestion]);
        
        NSArray *arr = [NSJSONSerialization JSONObjectWithData:[[error localizedRecoverySuggestion] dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];
        
        if(arr == nil)
        {
            [delegate finishWithStatus:NO withMessage:@"Networking Error"];
        }
        
        if([[arr valueForKey:@"status"] boolValue] == NO);
        {
            [delegate finishWithStatus:NO withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
        }
        
    }];
    
}

-(void) getTokenWithFromAccount:(NSString *)account withAmount :(NSString *)amount
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL: url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            amount,@"token[amount]",
                            account,@"token[account_number]",nil];
    
    NSLog(@"%@",params);
    
    [httpClient postPath:@"/token.json" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        NSLog(@"%@",arr);
        
        NSString * token = [arr valueForKey:@"token"];
        
        
        [delegate finishWithStatus:YES withToken:token];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"%@",error);
        
               
        
    }];
    
}
-(void) sendTokenWithToken:(NSString *)token withToAccount:(NSString *)account
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL: url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            token,@"token",
                            account,@"account",nil];
    
    NSLog(@"%@",params);
    
    [httpClient putPath:@"/token.json" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        NSLog(@"%@",arr);
        
        NSArray *messageArray = [arr valueForKey:@"message"];
        NSLog(@"Backup Value: %@",messageArray);
        
        [delegate finishWithStatus:YES withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"%@",error);
        
        NSArray *arr = [NSJSONSerialization JSONObjectWithData:[[error localizedRecoverySuggestion] dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];
        
        NSArray *messageArray = [arr valueForKey:@"message"];

         [delegate finishWithStatus:NO withMessage:[[arr valueForKey:@"message"] objectAtIndex:0]];
        
    }];
    
}
@end
