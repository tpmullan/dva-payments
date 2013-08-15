//
//  ContactStorage.h
//  DVA IOS
//
//  Created by Praetorian Guard on 8/9/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>
#import "Contact.h"

@interface ContactStorage : NSObject
{
    NSString *databasePath;
}

+(ContactStorage*)getSharedInstance;
-(BOOL)createDB;
//-(BOOL) hasContact:(NSString *) name;
-(BOOL) addContact:(NSString *) name withUsername:(NSString *) username withEmail:(NSString *) email;
-(BOOL) addContact:(Contact *) contact;
-(NSArray *) getContacts;
-(BOOL) cleanDatabase;



@end
