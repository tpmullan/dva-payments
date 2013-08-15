//
//  Account.m
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "Account.h"

@implementation Account


@synthesize name;
@synthesize number;
@synthesize balance;
@synthesize iid;

-(NSString *) description
{
    return [NSString stringWithFormat:@"Name:%@, Number:%@, Balance:%@",name,number,balance];
}
-(NSString*) getShortName
{
    return [NSString stringWithFormat:@"%@ (...%@)",name,[number substringFromIndex:[number length]-4]];
}
-(NSString*) getBalanceStr
{
    return [NSString stringWithFormat:@"$%@",balance];
}
-(NSString*) getName
{
    return name;
}
-(NSString*) getNumber
{
    return number;
}
@end
