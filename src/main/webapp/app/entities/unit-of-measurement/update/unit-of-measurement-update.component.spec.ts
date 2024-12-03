import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UnitOfMeasurementFormService } from './unit-of-measurement-form.service';
import { UnitOfMeasurementService } from '../service/unit-of-measurement.service';
import { IUnitOfMeasurement } from '../unit-of-measurement.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UnitOfMeasurementUpdateComponent } from './unit-of-measurement-update.component';

describe('UnitOfMeasurement Management Update Component', () => {
  let comp: UnitOfMeasurementUpdateComponent;
  let fixture: ComponentFixture<UnitOfMeasurementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let unitOfMeasurementFormService: UnitOfMeasurementFormService;
  let unitOfMeasurementService: UnitOfMeasurementService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UnitOfMeasurementUpdateComponent],
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
      .overrideTemplate(UnitOfMeasurementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UnitOfMeasurementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    unitOfMeasurementFormService = TestBed.inject(UnitOfMeasurementFormService);
    unitOfMeasurementService = TestBed.inject(UnitOfMeasurementService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const unitOfMeasurement: IUnitOfMeasurement = { id: 456 };
      const createdBy: IUser = { id: 94634 };
      unitOfMeasurement.createdBy = createdBy;
      const updatedBy: IUser = { id: 7914 };
      unitOfMeasurement.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 33638 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ unitOfMeasurement });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const unitOfMeasurement: IUnitOfMeasurement = { id: 456 };
      const createdBy: IUser = { id: 37035 };
      unitOfMeasurement.createdBy = createdBy;
      const updatedBy: IUser = { id: 96664 };
      unitOfMeasurement.updatedBy = updatedBy;

      activatedRoute.data = of({ unitOfMeasurement });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.unitOfMeasurement).toEqual(unitOfMeasurement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUnitOfMeasurement>>();
      const unitOfMeasurement = { id: 123 };
      jest.spyOn(unitOfMeasurementFormService, 'getUnitOfMeasurement').mockReturnValue(unitOfMeasurement);
      jest.spyOn(unitOfMeasurementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ unitOfMeasurement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: unitOfMeasurement }));
      saveSubject.complete();

      // THEN
      expect(unitOfMeasurementFormService.getUnitOfMeasurement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(unitOfMeasurementService.update).toHaveBeenCalledWith(expect.objectContaining(unitOfMeasurement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUnitOfMeasurement>>();
      const unitOfMeasurement = { id: 123 };
      jest.spyOn(unitOfMeasurementFormService, 'getUnitOfMeasurement').mockReturnValue({ id: null });
      jest.spyOn(unitOfMeasurementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ unitOfMeasurement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: unitOfMeasurement }));
      saveSubject.complete();

      // THEN
      expect(unitOfMeasurementFormService.getUnitOfMeasurement).toHaveBeenCalled();
      expect(unitOfMeasurementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUnitOfMeasurement>>();
      const unitOfMeasurement = { id: 123 };
      jest.spyOn(unitOfMeasurementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ unitOfMeasurement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(unitOfMeasurementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
