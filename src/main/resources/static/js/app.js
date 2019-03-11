'use strict';

angular.module('app', ['ngRoute', 'ngResource'])
.config(function ($routeProvider, $httpProvider) {
	$routeProvider
	.when('/login', {
		templateUrl: 'partials/login.html',
		controller: 'AuthenticationController',
		controllerAs: 'authController'
	})
	.when('/register', {
		templateUrl: 'partials/register.html',
		controller: 'RegistrationController',
		controllerAs: 'registerCtrl'
	})
		.when('/orderspricingdetails', {
		templateUrl: 'partials/orderspricingdetails.html',
		controller: 'OrdersPricingdetailsController',
		controllerAs: 'opCtrl'
	})
		.when('/contact', {
		templateUrl: 'partials/contact.html',
	})
		.when('/updates', {
		templateUrl: 'partials/updates.html',
	})
	.otherwise({
		redirectTo: 'orderspricingdetails'
	});
	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
})


.constant('LOGIN_ENDPOINT', '/login')
.constant('REGISTRATION_ENDPOINT', '/register')
.constant('PRICING_ENDPOINT', '/api/orders/pricing')//cennik
.constant('ORDERS_ENDPOINT', '/api/orders/user')//wszystkie ordery usera
.constant('OPENORDER_ENDPOINT', '/api/orders/open')//otwieranie
.constant('BALANCE_ENDPOINT', '/userbalance')//balans konta uzytkownika

.factory('OpenOrders', function($resource, ORDERS_ENDPOINT) {
	return $resource(ORDERS_ENDPOINT);
})
.factory('Pricing', function($resource, PRICING_ENDPOINT) {
	return $resource(PRICING_ENDPOINT);
})
.factory('NewOrder', function($resource, OPENORDER_ENDPOINT) {
	return $resource(OPENORDER_ENDPOINT);
})
.factory('User', function($resource, REGISTRATION_ENDPOINT) {
	return $resource(REGISTRATION_ENDPOINT);
})
.factory('Balance', function($resource, BALANCE_ENDPOINT) {
	return $resource(BALANCE_ENDPOINT);
})

.service('Pricings', function(Pricing,OpenOrders) {
	this.getAllPricings = function() {
		return Pricing.query();
	}
	this.getAllOpen = function() {
		return OpenOrders.query();
	}
	this.get = function(index) {
		return Pricing.get({id: index});
	}
	this.add = function(pricing) {
		pricing.$save();
	}
})

.service('Users', function(User) {
	this.add = function(user) {
		user.$save();
	}
})
.service('AuthenticationService', function($http, LOGIN_ENDPOINT) {
	this.authenticate = function(credentials, successCallback) {
		var authHeader = {Authorization: 'Basic ' + btoa(credentials.username+':'+credentials.password)};
		var config = {headers: authHeader};
		$http
		.post(LOGIN_ENDPOINT, {}, config)
		.then(function success(value) {
			$http.defaults.headers.common.Authorization = authHeader.Authorization;
			successCallback();
		}, function error(reason) {
			console.log('Login error');
			console.log(reason);
		});
	}
	this.logout = function(successCallback) {
		delete $http.defaults.headers.post.Authorization;
		successCallback();
	}
})

.controller('OrdersPricingdetailsController', function(Pricings, Pricing, NewOrder, $route) {
	var vm = this;
	
	function reloadData(){
		$route.reload();
	};
	
	//reloadData();
	
	vm.orders = Pricings.getAllPricings();
	
	vm.openOrder = new NewOrder();
	vm.openBuy = function(){
		vm.openOrder.openTime = vm.details.timeStamp;
		vm.openOrder.openPrice = vm.details.price;
		vm.openOrder.direction = "Buy";
		vm.openOrder.pair = vm.details.pair;
		
		Pricings.add(vm.openOrder)
		
		vm.order = new NewOrder();
		reloadData();
	}
	vm.openSell = function(){
		vm.openOrder.openTime = vm.details.timeStamp;
		vm.openOrder.openPrice = vm.details.price;
		vm.openOrder.direction = "Sell";
		vm.openOrder.pair = vm.details.pair;
		
		Pricings.add(vm.openOrder)
		
		vm.order = new NewOrder();
		reloadData();
	}
	vm.close = function(b){
		vm.openOrder.id = b.id;
		console.log(b.id);
		vm.openOrder.closeTime = vm.orders.timeStamp;
		vm.openOrder.closePrice = vm.orders.price;
		Pricings.add(vm.openOrder)
		vm.order = new NewOrder();
		reloadData();
	}
	vm.loadData = function(id) {
		vm.details = vm.orders.find(details => details.id === id);
	}

	vm.openOrders = Pricings.getAllOpen();

})
.controller('AuthenticationController', function($rootScope, $location, AuthenticationService) {
	var vm = this;
	vm.credentials = {};
	var loginSuccess = function() {
		$rootScope.authenticated = true;
		$location.path('/new');
	}
	vm.login = function() {
		AuthenticationService.authenticate(vm.credentials, loginSuccess);
	}
	var logoutSuccess = function() {
		$rootScope.authenticated = false;
		$location.path('/');
	}
	vm.logout = function() {
		AuthenticationService.logout(logoutSuccess);
	}
})
.controller('UserBalanceController', function(Balance) {
	var vm = this;
	vm.balans = Balance.query();
})
.controller('RegistrationController', function(Users, User) {
	var vm = this;
	vm.user = new User();
	vm.saveUser = function() {
		Users.add(vm.user);
		vm.user = new User();
	}
});