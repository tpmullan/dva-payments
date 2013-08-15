//
//  ContactStorage.m
//  DVA IOS
//
//  Created by Praetorian Guard on 8/9/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "ContactStorage.h"

static ContactStorage *sharedInstance = nil;
static sqlite3 *database = nil;
static sqlite3_stmt *statement = nil;

@implementation ContactStorage

+(ContactStorage*)getSharedInstance{
    if (!sharedInstance) {
        sharedInstance = [[super allocWithZone:NULL]init];
        [sharedInstance createDB];
    }
    return sharedInstance;
}

-(BOOL)createDB{
    NSString *docsDir;
    NSArray *dirPaths;
    // Get the documents directory
    dirPaths = NSSearchPathForDirectoriesInDomains
    (NSDocumentDirectory, NSUserDomainMask, YES);
    docsDir = dirPaths[0];
    // Build the path to the database file
    databasePath = [[NSString alloc] initWithString:
                    [docsDir stringByAppendingPathComponent: @"contacts.db"]];
    BOOL isSuccess = YES;
    NSFileManager *filemgr = [NSFileManager defaultManager];
    if ([filemgr fileExistsAtPath: databasePath ] == NO)
    {
        const char *dbpath = [databasePath UTF8String];
        if (sqlite3_open(dbpath, &database) == SQLITE_OK)
        {
            char *errMsg;
            const char *sql_stmt =
            "create table contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, name TEXT, email TEXT)";
            if (sqlite3_exec(database, sql_stmt, NULL, NULL, &errMsg)
                != SQLITE_OK)
            {
                isSuccess = NO;
                NSLog(@"Failed to create table");
            }
            sqlite3_close(database);
            return  isSuccess;
        }
        else {
            isSuccess = NO;
            NSLog(@"Failed to open/create database");
        }
    }
    return isSuccess;
}

- (BOOL) addContact:(NSString *)name withUsername:(NSString *)username withEmail:(NSString *)email
{
    NSLog(@"Added new contact %@", name);
    const char *dbpath = [databasePath UTF8String];
    if (sqlite3_open(dbpath, &database) == SQLITE_OK)
    {
        NSString *insertSQL = [NSString stringWithFormat:@"insert into contacts (name,username,email) VALUES ('%@','%@','%@')",name,username,email];
        const char *insert_stmt = [insertSQL UTF8String];
        sqlite3_prepare_v2(database, insert_stmt,-1, &statement, NULL);
        if (sqlite3_step(statement) == SQLITE_DONE)
        {
            sqlite3_close(database);
            return YES;
        }
        else {
            NSLog(@"SQLITE_DONE Not Done");
            sqlite3_close(database);
            return NO;
        }
        sqlite3_reset(statement);
    }
    NSLog(@"SQLite Not OK");
    return NO;
}
- (BOOL) addContact:(Contact *)contact
{
    return [self addContact:contact.name withUsername:contact.username withEmail:contact.email];
    
    
    
}

- (NSArray *) getContacts
{
    NSMutableArray *resultArray = [[NSMutableArray alloc] init];
    
    NSString *querySQL = @"select name,username,email from contacts";
    const char *dbpath = [databasePath UTF8String];
    const char *query_stmt = [querySQL UTF8String];
    if (sqlite3_open(dbpath, &database) == SQLITE_OK)
    {
        if( sqlite3_prepare_v2(database,query_stmt, -1, &statement, NULL) == SQLITE_OK)
        {
            while(sqlite3_step(statement) == SQLITE_ROW)
            {
                Contact * contact = [[Contact alloc] init];
                
                NSString *name = [[NSString alloc] initWithUTF8String:
                                  (const char *) sqlite3_column_text(statement, 0)];
                NSString *username = [[NSString alloc] initWithUTF8String:
                                      (const char *) sqlite3_column_text(statement, 1)];
                NSString *email = [[NSString alloc] initWithUTF8String:
                                   (const char *) sqlite3_column_text(statement, 2)];
                
                
                [contact setContactName:name withUsername:username withEmail:email];
                NSLog(@"Single Contact %@",contact);
                
                [resultArray addObject:contact];
                
            }
        }
    }
    
    NSLog(@"Dumping Contact Storage:%@",resultArray);
    
    sqlite3_close(database);
    return resultArray;
    
}

-(BOOL) cleanDatabase
{
    NSString *querySQL = @"drop table if exists contacts";
    const char *query_stmt = [querySQL UTF8String];
    
    if(sqlite3_prepare_v2(database,query_stmt,-1,&statement,NULL) == SQLITE_OK)
    {
        if (sqlite3_step(statement) == SQLITE_DONE)
        {
            char *errMsg;
            const char *sql_stmt =
            "create table contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, name TEXT, email TEXT)";
            if (sqlite3_exec(database, sql_stmt, NULL, NULL, &errMsg)
                != SQLITE_OK)
            {
                NSLog(@"Failed to create table");
            }
            sqlite3_close(database);
            
            return YES;
        }
        else {
            NSLog(@"SQLITE_DONE Not Done");
            return NO;
        }
        sqlite3_reset(statement);
        
    }
    
    return NO;
}


@end

