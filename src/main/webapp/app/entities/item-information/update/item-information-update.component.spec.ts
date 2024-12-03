import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemInformationFormService } from './item-information-form.service';
import { ItemInformationService } from '../service/item-information.service';
import { IItemInformation } from '../item-information.model';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IUnitOfMeasurement } from 'app/entities/unit-of-measurement/unit-of-measurement.model';
import { UnitOfMeasurementService } from 'app/entities/unit-of-measurement/service/unit-of-measurement.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ItemInformationUpdateComponent } from './item-information-update.component';

describe('ItemInformation Management Update Component', () => {
  let comp: ItemInformationUpdateComponent;
  let fixture: ComponentFixture<ItemInformationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemInformationFormService: ItemInformationFormService;
  let itemInformationService: ItemInformationService;
  let departmentService: DepartmentService;
  let unitOfMeasurementService: UnitOfMeasurementService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemInformationUpdateComponent],
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
      .overrideTemplate(ItemInformationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemInformationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemInformationFormService = TestBed.inject(ItemInformationFormService);
    itemInformationService = TestBed.inject(ItemInformationService);
    departmentService = TestBed.inject(DepartmentService);
    unitOfMeasurementService = TestBed.inject(UnitOfMeasurementService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Department query and add missing value', () => {
      const itemInformation: IItemInformation = { id: 456 };
      const department: IDepartment = { id: 8041 };
      itemInformation.department = department;

      const departmentCollection: IDepartment[] = [{ id: 19303 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemInformation });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UnitOfMeasurement query and add missing value', () => {
      const itemInformation: IItemInformation = { id: 456 };
      const unitOfMeasurement: IUnitOfMeasurement = { id: 24427 };
      itemInformation.unitOfMeasurement = unitOfMeasurement;

      const unitOfMeasurementCollection: IUnitOfMeasurement[] = [{ id: 97397 }];
      jest.spyOn(unitOfMeasurementService, 'query').mockReturnValue(of(new HttpResponse({ body: unitOfMeasurementCollection })));
      const additionalUnitOfMeasurements = [unitOfMeasurement];
      const expectedCollection: IUnitOfMeasurement[] = [...additionalUnitOfMeasurements, ...unitOfMeasurementCollection];
      jest.spyOn(unitOfMeasurementService, 'addUnitOfMeasurementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemInformation });
      comp.ngOnInit();

      expect(unitOfMeasurementService.query).toHaveBeenCalled();
      expect(unitOfMeasurementService.addUnitOfMeasurementToCollectionIfMissing).toHaveBeenCalledWith(
        unitOfMeasurementCollection,
        ...additionalUnitOfMeasurements.map(expect.objectContaining)
      );
      expect(comp.unitOfMeasurementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const itemInformation: IItemInformation = { id: 456 };
      const createdBy: IUser = { id: 61752 };
      itemInformation.createdBy = createdBy;
      const updatedBy: IUser = { id: 43835 };
      itemInformation.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 37219 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemInformation });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const itemInformation: IItemInformation = { id: 456 };
      const department: IDepartment = { id: 87498 };
      itemInformation.department = department;
      const unitOfMeasurement: IUnitOfMeasurement = { id: 97814 };
      itemInformation.unitOfMeasurement = unitOfMeasurement;
      const createdBy: IUser = { id: 13846 };
      itemInformation.createdBy = createdBy;
      const updatedBy: IUser = { id: 52590 };
      itemInformation.updatedBy = updatedBy;

      activatedRoute.data = of({ itemInformation });
      comp.ngOnInit();

      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.unitOfMeasurementsSharedCollection).toContain(unitOfMeasurement);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.itemInformation).toEqual(itemInformation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItemInformation>>();
      const itemInformation = { id: 123 };
      jest.spyOn(itemInformationFormService, 'getItemInformation').mockReturnValue(itemInformation);
      jest.spyOn(itemInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemInformation }));
      saveSubject.complete();

      // THEN
      expect(itemInformationFormService.getItemInformation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemInformationService.update).toHaveBeenCalledWith(expect.objectContaining(itemInformation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItemInformation>>();
      const itemInformation = { id: 123 };
      jest.spyOn(itemInformationFormService, 'getItemInformation').mockReturnValue({ id: null });
      jest.spyOn(itemInformationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemInformation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemInformation }));
      saveSubject.complete();

      // THEN
      expect(itemInformationFormService.getItemInformation).toHaveBeenCalled();
      expect(itemInformationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItemInformation>>();
      const itemInformation = { id: 123 };
      jest.spyOn(itemInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemInformationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDepartment', () => {
      it('Should forward to departmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUnitOfMeasurement', () => {
      it('Should forward to unitOfMeasurementService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(unitOfMeasurementService, 'compareUnitOfMeasurement');
        comp.compareUnitOfMeasurement(entity, entity2);
        expect(unitOfMeasurementService.compareUnitOfMeasurement).toHaveBeenCalledWith(entity, entity2);
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
