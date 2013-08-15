//
//  Account.h
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Account : NSObject
@property (nonatomic,strong) NSString* name;
@property (nonatomic,strong) NSString* number;
@property (nonatomic,strong) NSNumber* balance;
@property (nonatomic,strong) NSNumber* iid;

-(NSString*) description;
-(NSString*) getShortName;
-(NSString*) getBalanceStr;
-(NSString*) getName;
-(NSString*) getNumber;

@end
