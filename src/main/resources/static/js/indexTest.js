/**
 * Created by nxphi on 2/25/2017.
 */

var app = angular.module("indexTestApp",[]);
app.controller("indexTestController", ["$scope", "$rootScope", "$http", "$location", "$timeout",
function ($scope,  $rootScope, $http, $location, $timeout) {

	$scope.companies = [];

	// retrieve
	$http.get("rest/companies").success(function (res) {
		console.log(res);
		$scope.companies = res;
		$.each($scope.companies, function (index, comp) {
			comp.prodFormData = {
				prodId: "",
				prodName: "",
				prodCategory: "",
				price: "",
				stock: 0,
			}
		});
	}).error(function (res) {
		console.log("ERROR");
		console.log(res);
		alert("ERROR retrieve");
	});


	// upload function
	$scope.uploadImg = function (compId, prodId) {
		console.log("Uploading at comp " + compId + " and prod " + prodId);
		var formData = new FormData(document.getElementById("imgForm_" + compId + "_" + prodId));
		$http.post("/rest/product/" + prodId + "/uploadImg", formData, {
			transformRequest: angular.identity,
			headers: {"Content-Type": undefined}
		}).success(function (res) { // it is the product
			console.log(res);
			var comp = $.grep($scope.companies, function (obj) {
				return obj.id == compId;
			})[0];
			var prod = $.grep(comp.productList, function (obj) {
				return obj.id == prodId;
			});
			if (prod.length > 0) {
				prod[0].imageList = res.imageList;
			}
		}).error(function (res) {
			console.log(res);
			alert(res);
		})
	}


	// upload new product
	$scope.uploadProduct = function (comp) {
		var data = comp.prodFormData;
		//FIXME: make this more efficient
		if (data.prodId == "" || data.prodName == "" || data.prodCategory == "") {
			alert("Id, name, cat must be filled!");
			return;
		}
		$http.post("/rest/company/" + comp.id + "/add/product", comp.prodFormData)
			.success(function (res) {
				// company is response
				console.log(res);
				if (res) {
					comp.productList = res.productList;
				}
				else {
					alert("You Enter Product Id that existed!");
					return;
				}
			})
			.error(function (res) {
				console.log(res);
				alert("ERROR");
			});
		comp.prodFormData = {
			prodId: "",
			prodName: "",
			prodCategory: "",
			price: "",
			stock: 0,
		}
	}
}]);