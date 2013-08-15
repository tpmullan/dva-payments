//
//  MyFriendsRequest.m
//  DVA IOS
//
//  Created by Praetorian Guard on 8/8/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "MyFriendsRequest.h"
#import <AddressBook/AddressBook.h>
#import <AFNetworking/AFNetworking.h>
@implementation MyFriendsRequest

@synthesize delegate;


-(void) sendFriends
{
    ABAddressBookRef addressBookRef = ABAddressBookCreateWithOptions(NULL, NULL);
    
    if (ABAddressBookGetAuthorizationStatus() == kABAuthorizationStatusNotDetermined) {
        ABAddressBookRequestAccessWithCompletion(addressBookRef, ^(bool granted, CFErrorRef error) {
            // First time access has been granted, add the contact
            [self sendContactBookToServerWithBook:addressBookRef];
            
        });
    }
    else if (ABAddressBookGetAuthorizationStatus() == kABAuthorizationStatusAuthorized) {
        NSLog(@"%@",@"Got Premission for the rest of the time");
        [self sendContactBookToServerWithBook:addressBookRef];
        
    }
    else {
        // The user has previously denied access
        // Send an alert telling user to change privacy setting in settings app
        NSLog(@"%@",@"User Does not Like US :(");
    }
    
}

-(void) sendContactBookToServerWithBook:(ABAddressBookRef) addressBook
{

    NSArray * allPeople = (__bridge NSArray *)ABAddressBookCopyArrayOfAllPeople(addressBook);
    NSMutableArray* _allItems = [[NSMutableArray alloc] initWithCapacity:[allPeople count]];
    NSMutableArray* preparedEmails = [[NSMutableArray alloc] init];
    NSMutableString * emailsJSON = [[NSMutableString alloc] init];
    [emailsJSON appendString:@"["];
    for (id record in allPeople) {
        CFTypeRef emailProperty = ABRecordCopyValue((__bridge ABRecordRef)record, kABPersonEmailProperty);
        NSArray *emails = (__bridge NSArray *)ABMultiValueCopyArrayOfAllValues(emailProperty);
        CFRelease(emailProperty);
        for (NSString *email in emails) {
            NSString* compositeName = (__bridge NSString *)ABRecordCopyCompositeName((__bridge ABRecordRef)record);
            
            [preparedEmails addObject:email];
            [emailsJSON appendString:email];
            [emailsJSON appendString:@","];
            NSLog(@"%@",email);
            
            
            
        }

    }
    [emailsJSON appendString:@"]"];
    CFRelease(addressBook);
    allPeople = nil;
    
    NSLog(@"%@",emailsJSON);
    
    //Now we can make the network call to the system and respond with the correct values
    
    NSString * urlstr = [[NSUserDefaults standardUserDefaults] stringForKey:@"Url"];
    NSURL * url = [NSURL URLWithString:urlstr];
    
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL: url];
    
    httpClient.allowsInvalidSSLCertificate = YES;
    
    
    [httpClient setParameterEncoding:AFJSONParameterEncoding];
    
    
    
    
    
    NSDictionary * params = [NSDictionary dictionaryWithObjectsAndKeys:preparedEmails,@"emails", nil];
    
    NSLog(@"%@",params);
    
    [httpClient postPath:@"/friends.json" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        
        if([[arr valueForKey:@"status"] boolValue] == YES)
        {
            
            NSLog(@"Success: %@",[arr valueForKey:@"message"]);
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
    
    
    //next we get to update the local contact database from what the user has
    
    httpClient = [[AFHTTPClient alloc] initWithBaseURL: url];
        
    httpClient.allowsInvalidSSLCertificate = YES;
    
    [httpClient getPath:@"/friends.json" parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSArray *arr = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        
        NSLog(@"Friends on server: %@",arr);
        
        ContactStorage * contacts = [ContactStorage getSharedInstance];
        
        [contacts cleanDatabase];
        
        
        for(int k = 0; k < [arr count]; k++)
        {
            NSDictionary * contact = [arr objectAtIndex:k];
            
            bool result = [contacts addContact:[contact objectForKey:@"name"] withUsername:[contact objectForKey:@"username"] withEmail:[contact objectForKey:@"email"]];
            NSLog(@"Added Contact %@, did it finish?%@",contact,result ? @"Yes" : @"No");
        }
        
        
        [contacts getContacts];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
    }];
    
    
    

    
        
    
    
    
}

@end
