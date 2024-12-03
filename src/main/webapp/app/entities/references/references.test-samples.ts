import { RelationshipWithEmployee } from 'app/entities/enumerations/relationship-with-employee.model';

import { IReferences, NewReferences } from './references.model';

export const sampleWithRequiredData: IReferences = {
  id: 4091,
};

export const sampleWithPartialData: IReferences = {
  id: 89926,
};

export const sampleWithFullData: IReferences = {
  id: 73583,
  name: 'Berkshire',
  institute: 'implement Viaduct',
  designation: 'eco-centric Refined',
  relationshipWithEmployee: RelationshipWithEmployee['PROFESSIONAL'],
  email: 'Mauricio.White@gmail.com',
  contactNumber: 'Soap deposit',
};

export const sampleWithNewData: NewReferences = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
