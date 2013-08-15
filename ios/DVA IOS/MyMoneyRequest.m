//
//  MyMoneyRequest.m
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "MyMoneyRequest.h"
#import <AFNetworking/AFNetworking.h>

@implementation MyMoneyRequest
@synthesize delegate;

-(void) getTransactionsAll
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    

    
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL: url];
    
    httpClient.allowsInvalidSSLCertificate=YES;
    
    [httpClient getPath:@"/transactions.json" parameters:nil success:^(AFHTTPRequestOperation * operation, id responseObject)
     {
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
         NSLog(@"%@",arr);
         
         NSMutableArray * returnArray = [[NSMutableArray alloc] init];
         
         NSArray * transactions = [arr valueForKey:@"transactions"];
         for(int i = 0; i  < [transactions count]; i++)
         {
             NSArray * singleTransaction = [transactions objectAtIndex:i];
             Transaction * transaction = [[Transaction alloc] init];
             
             transaction.amount = (NSNumber *)[singleTransaction valueForKey:@"amount"];
             transaction.accountFrom = [self overwriteNSNull:[singleTransaction valueForKey:@"fromAccount"]];
             transaction.memo = [self overwriteNSNull:[singleTransaction valueForKey:@"memo"]];
             transaction.accountTo = [self overwriteNSNull:[singleTransaction valueForKey:@"toAccount"]];
             transaction.date = [self overwriteNSNull:[singleTransaction valueForKey:@"created_at"]];
             
             [returnArray addObject:transaction];
         }
         
         [delegate finishWithAllBalance:YES withResponse:returnArray];
         
     }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate finishWithAllBalance:NO withResponse:nil];
         
         
     }];
}
-(void) getTransactionsWithID:(NSNumber *) accountId
{
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL: url];
    
    httpClient.allowsInvalidSSLCertificate=YES;
    
    NSString * path = [NSString stringWithFormat:@"/transactions/%@.json",accountId];
    
    [httpClient getPath:path parameters:nil success:^(AFHTTPRequestOperation * operation, id responseObject)
     {
         NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
         NSLog(@"%@",arr);
         
         NSMutableArray * returnArray = [[NSMutableArray alloc] init];
         
         NSArray * transactions = [arr valueForKey:@"transactions"];
         for(int i = 0; i  < [transactions count]; i++)
         {
             NSArray * singleTransaction = [transactions objectAtIndex:i];
             Transaction * transaction = [[Transaction alloc] init];
             
             transaction.amount = (NSNumber *)[singleTransaction valueForKey:@"amount"];
             transaction.accountFrom = [self overwriteNSNull:[singleTransaction valueForKey:@"fromAccount"]];
             transaction.memo = [self overwriteNSNull:[singleTransaction valueForKey:@"memo"]];
             transaction.accountTo = [self overwriteNSNull:[singleTransaction valueForKey:@"toAccount"]];
             transaction.date = [self overwriteNSNull:[singleTransaction valueForKey:@"created_at"]];
             
             [returnArray addObject:transaction];
         }
         
         [delegate finishWithAllBalance:YES withResponse:returnArray];
         
     }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate finishWithAllBalance:NO withResponse:nil];
         
         
     }];
}

-(NSString *) overwriteNSNull:(NSString *) toCheck
{
    if (toCheck == (id)[NSNull null])
    {
        return @"";
    }
    else
    {
        return toCheck;
    }
}
@end
