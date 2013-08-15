//
//  AccountStorage.m
//  DVA IOS
//
//  Created by praetorian guard on 7/29/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "AccountStorage.h"

@implementation AccountStorage
@synthesize accounts;

static AccountStorage * sharedObject = nil;

+(AccountStorage *) sharedInstance
{
    if(sharedObject == nil)
    {
        sharedObject = [[super allocWithZone:NULL] init];
    }

return sharedObject;
}
+(NSString *) getAccountStr:(NSInteger *) index
{
    
    AccountStorage *shared = [AccountStorage sharedInstance];
    NSLog(@"Getting ACcount from shared storage:\n%@",shared.accounts);
    int value = index;
    return [[shared.accounts objectAtIndex:value] description];
}
+(Account *) getAccountWithIndex:(NSInteger*) index
{
    AccountStorage *shared = [AccountStorage sharedInstance];
    return [shared.accounts objectAtIndex:index];
}
+(NSUInteger *) getCount
{
    
    AccountStorage *shared = [AccountStorage sharedInstance];
    return [shared.accounts count];
}
+(void) addAccount:(Account*) account
{
    
    NSLog(@"Adding Account: %@",account);

    AccountStorage *shared = [AccountStorage sharedInstance];
    
    if(shared.accounts == nil)
    {
        shared.accounts = [[NSMutableArray alloc] init];
    }
    
    [shared.accounts addObject:account];
}




@end