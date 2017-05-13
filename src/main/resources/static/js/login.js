/**
 * Created by fi on 3/15/2017.
 * Login.js control Login page
 */

var loginApp = angular.module("loginApp", []);
loginApp.controller("loginController", function ($scope, $rootScope, $http, $timeout, $location) {
	$scope.loginForm = {
		data: {
			userName: "",
			password: "",
		},
		submit: function () {
			var _this = this;
			$http.post("/login", _this.data);
		}
	}
});
