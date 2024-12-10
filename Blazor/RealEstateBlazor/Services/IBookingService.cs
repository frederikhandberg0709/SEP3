using RealEstateBlazor.Data.DTOs;

namespace RealEstateBlazor.Services;

public interface IBookingService
{
    Task<bool> CreateBookingAsync(BookingDTO booking);
    Task<List<AgentDTO>> GetAvailableAgentsAsync(DateTime date);
}
