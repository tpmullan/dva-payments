//
//  Transaction.h
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Transaction : NSObject
@property (nonatomic,strong) NSString * accountTo;
@property (nonatomic,strong) NSString * accountFrom;
@property (nonatomic,strong) NSString * date;
@property (nonatomic,strong) NSString * memo;
@property (nonatomic,strong) NSNumber * amount;

-(NSString *) description;


@end
