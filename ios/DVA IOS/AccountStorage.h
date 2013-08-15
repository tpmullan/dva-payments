//
//  AccountStorage.h
//  DVA IOS
//
//  Created by praetorian guard on 7/29/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Account.h"

@interface AccountStorage : NSObject
{
    NSMutableArray * accounts;
}

@property(nonatomic,retain)NSMutableArray *accounts;
+(AccountStorage*)sharedInstance;
+(NSString *) getAccountStr:(NSUInteger *) index;
+(void) addAccount:(Account *) account;
+(Account *) getAccountWithIndex:(NSInteger *) index;
+(NSUInteger *) getCount;
@end

