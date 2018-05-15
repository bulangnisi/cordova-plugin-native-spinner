//
//  CDVSpinnerDialog.m
//
//  Created by Domonkos Pál on 2014.01.27..
//
//

#import "CDVSpinnerDialog.h"

@interface CDVSpinnerDialog () {
    UIActivityIndicatorView *indicator;
    NSString *callbackId;
    NSString *title;
    NSString *message;
    NSNumber *isFixed;
    NSString *alpha;
    NSString *red;
    NSString *green;
    NSString *blue;
}

@property (nonatomic, retain) UIActivityIndicatorView *indicator;
@property (nonatomic, retain) UIView *overlay;
@property (nonatomic, retain) UILabel *messageView;


@end

@implementation CDVSpinnerDialog

@synthesize indicator = _indicator;
@synthesize overlay = _overlay;
@synthesize messageView = _messageView;

-(CGRect)rectForView {
    if ((NSFoundationVersionNumber <= 1047.25 /* 7.1 */) && UIInterfaceOrientationIsLandscape([UIApplication sharedApplication].statusBarOrientation)) {
        return CGRectMake( 0.0f, 0.0f, [[UIScreen mainScreen]bounds].size.height, [UIScreen mainScreen].bounds.size.width);
    }
    return CGRectMake( 0.0f, 0.0f, [[UIScreen mainScreen]bounds].size.width, [UIScreen mainScreen].bounds.size.height);
}

- (void)handleTapGesture:(UITapGestureRecognizer *)gesture
{
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [result setKeepCallbackAsBool:true];
    if (!isFixed.boolValue) {
        [result setKeepCallbackAsBool:false];
        [self hide];
    }
    [self.commandDelegate sendPluginResult:result callbackId:callbackId];
}

- (UIView *)overlay {
    if (!_overlay) {
        _overlay = [[UIView alloc] initWithFrame:self.rectForView];
        _overlay.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha: 0.3];
        
//        _indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
//        _indicator.center = _overlay.center;
//        [_indicator startAnimating];
//        [_overlay addSubview:_indicator];
        
        NSString *path = [[NSBundle mainBundle] pathForResource:@"gif_loading" ofType:@"gif"];
        NSData *gifData = [NSData dataWithContentsOfFile:path];
        CGRect r1 = CGRectMake(0,0,230,230);
//        UIWebView *webView = [[UIWebView alloc] initWithFrame:_overlay.bounds];
        UIWebView *webView = [[UIWebView alloc] initWithFrame:r1];
        webView.scalesPageToFit = YES;
        [webView loadData:gifData MIMEType:@"image/gif" textEncodingName:nil baseURL:nil];
        webView.backgroundColor = [UIColor clearColor];
        webView.opaque = NO;
        webView.scrollView.bounces = NO;
        
        webView.center = _overlay.center;
        
        
//        NSString *injectionJSString = @"var script = document.createElement('meta');"
//        "script.name = 'viewport';"
//        "script.content=\"width=device-width, initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\";"
//        "document.getElementsByTagName('head')[0].appendChild(script);";
//        [webView stringByEvaluatingJavaScriptFromString:injectionJSString];
        
        [_overlay addSubview:webView];

        _messageView = [[UILabel alloc] initWithFrame: self.rectForView];
        [_messageView setText: message == nil ? title : message];
        [_messageView setTextColor: [UIColor colorWithRed:[red floatValue] green:[green floatValue] blue:[blue floatValue] alpha:0.65]];
        [_messageView setBackgroundColor: [UIColor colorWithRed:0 green:0 blue:0 alpha:0]];
        [_messageView setTextAlignment: NSTextAlignmentCenter];
        _messageView.center = (CGPoint){_overlay.center.x, _overlay.center.y + 40};
        _messageView.font = [UIFont fontWithName:@"Helvetica" size:(10.0)];
        _messageView.lineBreakMode = UILineBreakModeWordWrap;
        _messageView.numberOfLines = 0;
        [_overlay addSubview:_messageView];

        UITapGestureRecognizer *tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapGesture:)];
        [_overlay addGestureRecognizer: tapRecognizer];
    }
    return _overlay;
}

- (void) show:(CDVInvokedUrlCommand*)command {

    callbackId = command.callbackId;

    title = [command argumentAtIndex:0];
    message = [command argumentAtIndex:1];
    isFixed = [command argumentAtIndex:2];
    alpha = [command argumentAtIndex:3];
    red = [command argumentAtIndex:4];
    green = [command argumentAtIndex:5];
    blue = [command argumentAtIndex:6];
    
    UIViewController *rootViewController = [[[[UIApplication sharedApplication] delegate] window] rootViewController];

    [rootViewController.view addSubview:self.overlay];

}

- (void) hide:(CDVInvokedUrlCommand*)command {
    [self hide];
}

- (void) hide {
    if (_overlay) {
        [self.indicator stopAnimating];
        [self.indicator removeFromSuperview];
        [self.messageView removeFromSuperview];
        [self.overlay removeFromSuperview];
        _indicator = nil;
        _messageView = nil;
        _overlay = nil;
    }
}

#pragma mark - PRIVATE METHODS


@end


