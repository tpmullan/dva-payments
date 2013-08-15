//
//  BalanceViewController.h
//  DVA IOS
//
//  Created by Tom M. on 8/7/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyAccount.h"
@interface BalanceViewController : UIViewController
<UITableViewDelegate, UITableViewDataSource, MyAccountDelegate>
{
    
}
@property (weak, nonatomic) IBOutlet UILabel *AccountName;
@property (weak, nonatomic) IBOutlet UILabel *AccountBalance;
@property (nonatomic, strong) NSString *AccountID;
@property (nonatomic, strong) IBOutlet UITableView *tableView;


@end
