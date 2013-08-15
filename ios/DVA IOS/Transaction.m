//
//  Transaction.m
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "Transaction.h"

@implementation Transaction

@synthesize accountFrom;
@synthesize accountTo;
@synthesize amount;
@synthesize date;
@synthesize memo;

-(NSString *) description
{
    return [NSString stringWithFormat:@"Date:%@\nAccount From:%@\nAccount To: %@\nAmount%@\nMemo: %@",date,accountFrom,accountTo,amount,memo];
}


@end
