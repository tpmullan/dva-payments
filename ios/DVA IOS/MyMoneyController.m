//
//  MyMoneyController.m
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "MyMoneyController.h"

@interface MyMoneyController ()

@end

@implementation MyMoneyController{
    NSArray *transactions;
}
@synthesize AccountHeader;
@synthesize balance;
@synthesize AccountTableView;
@synthesize myAccount;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
-(void) loadTransactions
{
    MyMoneyRequest * client = [[MyMoneyRequest alloc] init];
    
    [client setDelegate:self];
    [client getTransactionsWithID:myAccount.iid];
}
-(void) finishWithAllBalance:(BOOL)status withResponse:(NSMutableArray *)response
{
    NSLog(@"%@",response);
    transactions = response;
    [self.AccountTableView reloadData];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [AccountHeader setText:myAccount.getShortName];
    [balance setText:[NSString stringWithFormat:@"$%@",myAccount.balance]];
	[self loadTransactions];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [transactions count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"tranCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    Transaction *tranLoc = [transactions objectAtIndex:indexPath.row];
    //set info
    UILabel *label;
    label = (UILabel *)[cell viewWithTag:1];
    label.text = tranLoc.accountTo;
    label = (UILabel *)[cell viewWithTag:2];
    label.text = tranLoc.accountFrom;
    label = (UILabel *)[cell viewWithTag:3];
    label.text = [NSString stringWithFormat:@"%@",tranLoc.amount];
    label = (UILabel *)[cell viewWithTag:4];
    label.text = tranLoc.memo;
    label = (UILabel *)[cell viewWithTag:5];
    label.text = tranLoc.date;
    return cell;
}

@end
