//
//  MyMoneyRequest.h
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "Transaction.h"
#import <Foundation/Foundation.h>

@class MyMoneyRequest;
@protocol MyMoneyRequestDelegate<NSObject>
@optional
-(void) finishWithAllBalance:(BOOL) status withResponse:(NSMutableArray *) response;
@end

@interface MyMoneyRequest : NSObject
{
    id<MyMoneyRequestDelegate> delegate;
}

@property(nonatomic,retain) id<MyMoneyRequestDelegate> delegate;
- (void) getTransactionsAll;
- (void) getTransactionsWithID:(NSNumber *) accountId;
- (NSString *) overwriteNSNull:(NSString *) toCheck;
@end
