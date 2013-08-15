//
//  BalanceViewController.m
//  DVA IOS
//
//  Created by Tom M. on 8/7/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import "BalanceViewController.h"
#import "MyMoneyController.h"

@interface BalanceViewController ()
@end

@implementation BalanceViewController {
    NSArray *accounts;
}

@synthesize AccountID;
@synthesize AccountBalance;
@synthesize AccountName;
@synthesize tableView;

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Initialize table data
    [self getAccounts ];
}

-(void) getAccounts
{
    MyAccount * client = [[MyAccount alloc] init];
    
    [client setDelegate:self];
    
    [client getBalance];
} 
-(void) finishWithBalance:(NSMutableArray *)response
{
    accounts = response;
    //[AccountName setText:[[response objectAtIndex:0] getShortName]];
    //[AccountBalance setText:[[response objectAtIndex:0] getBalanceStr]];
    [self.tableView reloadData];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    return [accounts count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"AccountCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    Account *accountLoc = [accounts objectAtIndex:indexPath.row];
    //set info
    UILabel *label;
    label = (UILabel *)[cell viewWithTag:1];
    label.text = [accountLoc getShortName];
    label = (UILabel *)[cell viewWithTag:2];
    label.text = [accountLoc getBalanceStr];    //cell.textLabel.text = [accountLoc getName];
    return cell;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"showAccountDetail"]) {
        //UITableViewCell *cell = (UITableViewCell *)sender;
        NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
        MyMoneyController *destViewController = segue.destinationViewController;
        destViewController.MyAccount = [accounts objectAtIndex:indexPath.row];
    }
}

@end
