//
//  MoveMoneyRequest.h
//  DVA IOS
//
//  Created by praetorian guard on 7/29/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>

@class MoveMoneyRequest;

@protocol MoveMoneyRequestDelegate<NSObject>
@optional
-(void) finishWithStatus:(BOOL) status withMessage:(NSString *) message;
-(void) finishWithStatus:(BOOL) status withToken:(NSString*) token;

@end
@interface MoveMoneyRequest : NSObject
{
    id<MoveMoneyRequestDelegate> delegate;
}

@property(nonatomic,retain) id<MoveMoneyRequestDelegate> delegate;
-(void) sendTransferwithToAccount: (NSString *) toAccount withFromAccount:(NSString*) fromAccount withAmount:(NSNumber *) amount;
-(void) getTokenWithFromAccount: (NSString *) account withAmount:(NSString *) amount;
-(void) sendTokenWithToken: (NSString *) token withToAccount:(NSString*) account;

@end
