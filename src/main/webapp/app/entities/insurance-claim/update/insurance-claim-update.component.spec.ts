import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InsuranceClaimFormService } from './insurance-claim-form.service';
import { InsuranceClaimService } from '../service/insurance-claim.service';
import { IInsuranceClaim } from '../insurance-claim.model';
import { IInsuranceRegistration } from 'app/entities/insurance-registration/insurance-registration.model';
import { InsuranceRegistrationService } from 'app/entities/insurance-registration/service/insurance-registration.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { InsuranceClaimUpdateComponent } from './insurance-claim-update.component';

describe('InsuranceClaim Management Update Component', () => {
  let comp: InsuranceClaimUpdateComponent;
  let fixture: ComponentFixture<InsuranceClaimUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let insuranceClaimFormService: InsuranceClaimFormService;
  let insuranceClaimService: InsuranceClaimService;
  let insuranceRegistrationService: InsuranceRegistrationService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InsuranceClaimUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InsuranceClaimUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InsuranceClaimUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    insuranceClaimFormService = TestBed.inject(InsuranceClaimFormService);
    insuranceClaimService = TestBed.inject(InsuranceClaimService);
    insuranceRegistrationService = TestBed.inject(InsuranceRegistrationService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InsuranceRegistration query and add missing value', () => {
      const insuranceClaim: IInsuranceClaim = { id: 456 };
      const insuranceRegistration: IInsuranceRegistration = { id: 7601 };
      insuranceClaim.insuranceRegistration = insuranceRegistration;

      const insuranceRegistrationCollection: IInsuranceRegistration[] = [{ id: 402 }];
      jest.spyOn(insuranceRegistrationService, 'query').mockReturnValue(of(new HttpResponse({ body: insuranceRegistrationCollection })));
      const additionalInsuranceRegistrations = [insuranceRegistration];
      const expectedCollection: IInsuranceRegistration[] = [...additionalInsuranceRegistrations, ...insuranceRegistrationCollection];
      jest.spyOn(insuranceRegistrationService, 'addInsuranceRegistrationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ insuranceClaim });
      comp.ngOnInit();

      expect(insuranceRegistrationService.query).toHaveBeenCalled();
      expect(insuranceRegistrationService.addInsuranceRegistrationToCollectionIfMissing).toHaveBeenCalledWith(
        insuranceRegistrationCollection,
        ...additionalInsuranceRegistrations.map(expect.objectContaining)
      );
      expect(comp.insuranceRegistrationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const insuranceClaim: IInsuranceClaim = { id: 456 };
      const createdBy: IUser = { id: 76660 };
      insuranceClaim.createdBy = createdBy;
      const updatedBy: IUser = { id: 6310 };
      insuranceClaim.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 1422 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ insuranceClaim });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const insuranceClaim: IInsuranceClaim = { id: 456 };
      const insuranceRegistration: IInsuranceRegistration = { id: 52221 };
      insuranceClaim.insuranceRegistration = insuranceRegistration;
      const createdBy: IUser = { id: 53599 };
      insuranceClaim.createdBy = createdBy;
      const updatedBy: IUser = { id: 14393 };
      insuranceClaim.updatedBy = updatedBy;

      activatedRoute.data = of({ insuranceClaim });
      comp.ngOnInit();

      expect(comp.insuranceRegistrationsSharedCollection).toContain(insuranceRegistration);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.insuranceClaim).toEqual(insuranceClaim);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceClaim>>();
      const insuranceClaim = { id: 123 };
      jest.spyOn(insuranceClaimFormService, 'getInsuranceClaim').mockReturnValue(insuranceClaim);
      jest.spyOn(insuranceClaimService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceClaim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceClaim }));
      saveSubject.complete();

      // THEN
      expect(insuranceClaimFormService.getInsuranceClaim).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(insuranceClaimService.update).toHaveBeenCalledWith(expect.objectContaining(insuranceClaim));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceClaim>>();
      const insuranceClaim = { id: 123 };
      jest.spyOn(insuranceClaimFormService, 'getInsuranceClaim').mockReturnValue({ id: null });
      jest.spyOn(insuranceClaimService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceClaim: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insuranceClaim }));
      saveSubject.complete();

      // THEN
      expect(insuranceClaimFormService.getInsuranceClaim).toHaveBeenCalled();
      expect(insuranceClaimService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsuranceClaim>>();
      const insuranceClaim = { id: 123 };
      jest.spyOn(insuranceClaimService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insuranceClaim });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(insuranceClaimService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInsuranceRegistration', () => {
      it('Should forward to insuranceRegistrationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(insuranceRegistrationService, 'compareInsuranceRegistration');
        comp.compareInsuranceRegistration(entity, entity2);
        expect(insuranceRegistrationService.compareInsuranceRegistration).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
