package com.example.woodscraft.DataModels

import okhttp3.MultipartBody
import java.io.File

data class Users(
    val _id: String?,
    val fullName: String,
    val email: String,
    val age: Int,
    val address: String,
    val admin: Boolean,
    val __v: Int?
)

data class Data(
    val User: Users,
    val AccessToken: String,
    val RefreshToken: String
)

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)

data class RegisterUser(
    val fullName: String,
    val email: String,
    val  age: Int,
    val address: String,
    val passkey: String
)

data class RegisterResponse(
    val statusCode: Int,
    val data: Data,
    val message: String,
    val success: Boolean
)

data class LoginUser(
    val email: String,
    val passkey: String
)

data class LoginResponse(
    val statusCode: Int,
    val data: Data,
    val message: String,
    val success: Boolean
)

// All product data models
data class Product(
    val price: Price,
    val _id: String?,
    val name: String?,
    val productId: String?,
    val images: List<String>,
    val description: String?,
    val summery: String?,
    val rating: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?
)
data class Price(
    val wasPrice: Int,
    val currentPrice: Int
)

data class ListOfProduct(
    val statusCode: Int,
    val data: List<Product>,
    val message: String,
    val success: Boolean,
)

// one product details data models
data class OneProduct(
    val statusCode: Int,
    val data: Productone,
    val message: String,
    val success: Boolean,
)

data class Prices(
    val wasPrice: Int,
    val currentPrice: Int
)

data class Productone(
    val price: Prices,
    val _id: String?,
    val name: String?,
    val productId: String?,
    val images: List<String>,
    val description: String?,
    val summery: String?,
    val rating: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?
)

// Adding and deleting from wishlist like button

data class AddWishlistResponse(
    val statusCode: Int,
    val data: Liked,
    val message: String,
    val success: Boolean
)
data class Liked(
    val _id: String,
    val user: String,
    val likedProduct: String,
    val createdAt: String,
    val updatedAt: String,
)
data class RemoveWishlistResponse(
    val statusCode: Int,
    val data: Liked,
    val message: String,
    val success: Boolean
)

// Get all wishlist

data class WishListResponse(
    val statusCode: Int,
    val data: List<LikedProduct>,
    val message: String,
    val success: Boolean
)

data class Pricess(
    val wasPrice: Int,
    val currentPrice: Int
)

data class LikedProduct(
    val price: Pricess,
    val _id: String?,
    val name: String?,
    val productId: String?,
    val images: List<String>,
    val description: String?,
    val summery: String?,
    val rating: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?
)

// Adding and deleting from cart

data class AddToCartResponse(
    val statusCode: Int,
    val data: Cart,
    val message: String,
    val success: Boolean
)

data class Cart(
    val _id: String,
    val user: String,
    val likedProduct: String,
    val createdAt: String,
    val updatedAt: String,
)

data class RemoveFromCartResponse(
    val statusCode: Int,
    val data: Liked,
    val message: String,
    val success: Boolean
)

data class CartResponse(
    val statusCode: Int,
    val data: List<CartProduct>,
    val message: String,
    val success: Boolean
)
data class PricesCart(
    val wasPrice: Int,
    val currentPrice: Int
)

data class CartProduct(
    val price: PricesCart,
    val _id: String?,
    val name: String?,
    val productId: String?,
    val images: List<String>,
    val description: String?,
    val summery: String?,
    val rating: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?
)

// Adding new product by Admin

data class ProductItems(
//    val images: File,
    val productId: String,
    val name: String,
    val description: String,
    val summery: String,
    val oldPrice: String,
    val newPrice: String,
    val rating: String,
)

data class NewPrice(
    val wasPrice: Int,
    val currentPrice: Int
)

data class NewProduct(
    val price: NewPrice,
    val _id: String?,
    val name: String?,
    val productId: String?,
    val images: List<String>,
    val description: String?,
    val summery: String?,
    val rating: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val __v: Int?
)

data class ProductItemResponse(
    val statusCode: Int,
    val data: NewProduct,
    val message: String,
)

data class DeleteProductResponse(
    val statusCode: Int,
    val message: String,
    val success: Boolean
)



