export type UserDetailsForm = Omit<UserDetails, 'id' | 'roles' | 'email'>;

export type UserDetails = {
    id: number | null;
    personalId: string;
    name: string;
    surname: string;
    phoneNumber: string;
    email: string;
    address: UserDetailsAddress;
    roles: string[];
};

export type UserDetailsAddress = {
    voivodeshipId: number | null;
    city: string;
    postalCode: string;
    street: string;
    buildingNumber: string;
};
