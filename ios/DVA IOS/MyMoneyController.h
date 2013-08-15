//
//  MyMoneyController.h
//  DVA IOS
//
//  Created by praetorian guard on 7/28/13.
//  Copyright (c) 2013 praetorian. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyMoneyRequest.h"
#import "Account.h"
#import "Transaction.h"

@interface MyMoneyController : UIViewController
<UITableViewDelegate, UITableViewDataSource, MyMoneyRequestDelegate>
{
    
}
@property (weak, nonatomic) IBOutlet UILabel *AccountHeader;
@property (weak, nonatomic) IBOutlet UILabel *balance;
@property (weak, nonatomic) IBOutlet UITableView *AccountTableView;
@property (weak, nonatomic) Account *myAccount;


@end
