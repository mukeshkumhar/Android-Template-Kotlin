package com.example.woodscraft.Routes

import com.example.woodscraft.DataModels.AddToCartResponse
import com.example.woodscraft.DataModels.AddWishlistResponse
import com.example.woodscraft.DataModels.CartResponse
import com.example.woodscraft.DataModels.DeleteProductResponse
import com.example.woodscraft.DataModels.ListOfProduct
import com.example.woodscraft.DataModels.LoginResponse
import com.example.woodscraft.DataModels.LoginUser
import com.example.woodscraft.DataModels.ProductItemResponse
import com.example.woodscraft.DataModels.ProductItems
import com.example.woodscraft.DataModels.RegisterResponse
import com.example.woodscraft.DataModels.RegisterUser
import com.example.woodscraft.DataModels.RemoveFromCartResponse
import com.example.woodscraft.DataModels.RemoveWishlistResponse
import com.example.woodscraft.DataModels.WishListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("user/register") fun registerUser(@Body user: RegisterUser): Call<RegisterResponse>
    @POST("user/login") fun loginUser(@Body user: LoginUser): Call<LoginResponse>
    @GET("products/new-product") fun getAllProduct(): Call<ListOfProduct>
    @POST("carts/wishlist/{productId}") fun addToWishlist(@Path("productId") productId: String): Call<AddWishlistResponse>
    @DELETE("carts/wishlist/{productId}") fun removeFromWishlist(@Path("productId") productId: String): Call<RemoveWishlistResponse>
    @GET("carts/wishlist/") fun getWishList(): Call<WishListResponse>
    @POST("carts/cart/{productId}") fun addToCart(@Path("productId") productId: String): Call<AddToCartResponse>
    @DELETE("carts/cart/{productId}") fun removeFromCart(@Path("productId") productId: String): Call<RemoveFromCartResponse>
    @GET("carts/cart/") fun getCart(): Call<CartResponse>
//    @POST("products/new-product") fun addProduct(@Body user: ProductItems): Call<ProductItemResponse>
    @Multipart
    @POST("products/new-product") fun addProduct(
        @Part image: MultipartBody.Part,
        @Part("productId") productId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("summery") summery: RequestBody,
        @Part("oldPrice") oldPrice: RequestBody,
        @Part("newPrice") newPrice: RequestBody,
        @Part("rating") rating: RequestBody,
    ): Call<ProductItemResponse>



    @DELETE("products/update-product-details/{productId}") fun deleteProduct(@Path("productId") productId: String): Call<DeleteProductResponse>

}