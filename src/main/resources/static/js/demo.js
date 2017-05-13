/**
 * Created by fi on 4/6/2017.
 */


var demoApp = angular.module("demoApp", []);
demoApp.controller("demoController", function ($scope, $http, $location, $timeout) {

	window.scope = $scope;
	$scope.rootUser = rootUser;
	// $scope.rootComp = rootComp;
	if (!rootUser) {
		alert("No root user");
	}

	// retrieve companies
	$scope.companies = [];
	$http.get("/rest/companies")
		.success(function (res) {
			$scope.companies = res;
		})
		.error(function (res) {
			console.log(res);
			alert("POST ERROR GET COMPANIES");
		});


	$scope.purchaseModule = {
		data: {
			user: $scope.rootUser,
			productList: [], // to be derive from saleList
			saleList: [],
		},
		formData: {
			company: null,
			product: null,
			theCompany: null,
			theProduct: null,
			quantity: 0,
			descript: ""
		},
		submit: function() {
			// get the product
			var _this = this;
			if (_this.data.saleList.length == 0) {
				alert("you must buy something");
				return;
			}
			$.each(_this.data.saleList, function (index, obj) {
				_this.data.productList.push(obj.product);
			});

			// submit
			$http.post("/rest/company/" + 1 + "/user/" + $scope.rootUser.id + "/purchase", _this.data)
				.success(function (res) {
					if (res) {
						alert("Success");
						window.location.reload();
					}
					else {
						console.log(res);
						alert("ERROR");
					}
				})
				.error(function (res) {
					console.log(res);
					alert("POST ERROR");
				})
		},
		setTheCompany: function () {
			console.log("this is called");
			this.formData.theCompany = $scope.companies[this.formData.company - 1]; // it the company id  - 1
		},
		setTheProduct: function () {
			var _this = this;
			var product = $.grep(this.formData.theCompany.productList, function (obj) {
				return obj.id == _this.formData.product;
			})[0];
			this.formData.price = product.price;
		},
		delItem: function (index) {
			if (index > -1) {
				this.data.saleList.splice(index, 1);
			}
		},
		addItem: function () {
			var _this = this;
			if (!_this.formData.company || !_this.formData.product) {
				alert("You must select company and product!");
				return;
			}

			var company = $.grep($scope.companies, function (obj) {
				return obj.id == _this.formData.company;
			})[0];

			var product = $.grep(company.productList, function (obj) {
				return obj.id == _this.formData.product;
			})[0];

			var newSale = {
				descript: _this.formData.descript,
				quantity: _this.formData.quantity,
				price: _this.formData.price,
				product: product,
				company: company,
			};

			_this.data.saleList.push(newSale);
		}
	}
});