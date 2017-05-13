/**
 * Created by fi on 4/4/2017.
 */

var compCreateApp = angular.module("compCreateApp", []);
compCreateApp.controller("compCreateController", function ($scope, $rootScope, $http, $location, $timeout) {

	$scope.companies = [];
	$http.get("/rest/companies", {})
		.success(function (res) {
			$scope.companies = res;
			console.log(res);
		})
		.error(function (res) {
			console.log(res);
			alert("POSt error");
		});

	$scope.createComp = {
		data: {
			companyName: "",
			category :"",
			address: "",
			postalCode: "",
		},
		submit: function () {
			// var formData = new formData(document.getElementById("newComp"));
			// console.log($("form#newComp").serialize())
			console.log(this.data);
			var  _this = this;
			$http.post("/rest/company/create", _this.data)
				.success(function (res) {
					if (res) {
						// success
						window.location = "/company/" + res.companyName;
					}
					else {
						alert("Your Company name is occupied!");
					}
				})
				.error(function (res) {
					console.log(res);
				})
		}
	};

	window.scope = $scope;
});