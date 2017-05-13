/**
 * Created by fi on 3/15/2017.
 * company
 */

function initProductList(prodList) {
	$.each(prodList, function (index, obj) {
		obj.onNewImg = false;
		$.each(obj.imageList, function (index, imgObj) {
			imgObj.onShow = false;
		})
	});
}

var compApp = angular.module("compApp", []);
compApp.controller("compController", function ($scope, $rootScope, $http, $timeout, $location) {
	if (!rootComp) {
		alert("Root company data not found!");
	}
	$scope.rootComp = rootComp;

	rootComp.prodFormData = {
		prodId: "",
		prodName: "",
		prodCategory: "",
		price: "",
		stock: 0,
	};

	initProductList($scope.rootComp.productList);
	// uploading image
	// upload function
	$scope.uploadImg = function (compId, prodId) {
		console.log("Uploading at comp " + compId + " and prod " + prodId);
		var formData = new FormData(document.getElementById("imgForm_" + compId + "_" + prodId));
		console.log($("form#imgForm_" + compId + "_" + prodId).serialize());
		$http.post("/rest/product/" + prodId + "/uploadImg", formData, {
			transformRequest: angular.identity,
			headers: {"Content-Type": undefined}
		}).success(function (res) { // it is the product
			console.log(res);
			// var comp = $.grep($scope.companies, function (obj) {
			// 	return obj.id == compId;
			// })[0];
			var prod = $.grep(rootComp.productList, function (obj) {
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
					initProductList($scope.rootComp.productList);

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
	};

	// upload multiple product with iamges
	$scope.uploadMultipleProductWithImage = function () {
		var path = "/rest/company/" + rootComp.id + "/add/product/multiple";
		var formData = new FormData(document.getElementById("newProdMultiple_img"));
		$http.post(path, formData, {
			transformRequest: angular.identity,
			headers: {"Content-Type": undefined}
		}).success(function (res) {
			console.log(res);
			rootComp.productList = res.productList;
			// window.location.reload();
		}).error(function (res) {
			console.log(res);
			alert(res);
		})
	}

});