/**
 *
 * Created by fi on 4/6/2017.
 */



var analysisApp = angular.module("analysisApp", []);
analysisApp.controller("analysisController", function ($scope, $http, $location, $timeout) {

	$scope.rootComp = rootComp;
	$scope.rootUser = rootUser;

	$scope.saleList=  [];
	$.each($scope.rootComp.productList, function (index, sale) {
		$scope.saleList.push(sale);
	})
});