import { Gender } from './gender.model';

export type UserDetailsForm = Omit<UserDetails, 'id' | 'roles' | 'email' | 'sex'>;

export type UserDetails = {
    id: number | null;
    personalId: string;
    name: string;
    surname: string;
    phoneNumber: string;
    email: string;
    address: UserDetailsAddress;
    roles: string[];
    sex: Gender;
};

export type UserDetailsAddress = {
    voivodeshipId: number | null;
    city: string;
    postalCode: string;
    street: string;
    buildingNumber: string;
};
