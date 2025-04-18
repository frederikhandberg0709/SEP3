﻿namespace RealEstateBlazor.Data.Models;

public class Login
{
    public long AccountId { get; set; }
    public string Username { get; set; }
    public string Password { get; set; }
    public string Role { get; set; } = "USER"; 
    public UserProfile UserProfile { get; set; }
}
