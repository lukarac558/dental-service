export type UserDetails = {
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    zipCode: string;
    city: string;
    address: string;
    gender: number;
    role: number;
};

export type UserDetailsForm = Omit<UserDetails, 'role'>
