import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VehicleFormService } from './vehicle-form.service';
import { VehicleService } from '../service/vehicle.service';
import { IVehicle } from '../vehicle.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { VehicleUpdateComponent } from './vehicle-update.component';

describe('Vehicle Management Update Component', () => {
  let comp: VehicleUpdateComponent;
  let fixture: ComponentFixture<VehicleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleFormService: VehicleFormService;
  let vehicleService: VehicleService;
  let userService: UserService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VehicleUpdateComponent],
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
      .overrideTemplate(VehicleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleFormService = TestBed.inject(VehicleFormService);
    vehicleService = TestBed.inject(VehicleService);
    userService = TestBed.inject(UserService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const vehicle: IVehicle = { id: 456 };
      const createdBy: IUser = { id: 89457 };
      vehicle.createdBy = createdBy;
      const updatedBy: IUser = { id: 88955 };
      vehicle.updatedBy = updatedBy;
      const approvedBy: IUser = { id: 3063 };
      vehicle.approvedBy = approvedBy;

      const userCollection: IUser[] = [{ id: 73628 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy, approvedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const vehicle: IVehicle = { id: 456 };
      const assignedDriver: IEmployee = { id: 56991 };
      vehicle.assignedDriver = assignedDriver;

      const employeeCollection: IEmployee[] = [{ id: 33406 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [assignedDriver];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vehicle: IVehicle = { id: 456 };
      const createdBy: IUser = { id: 28895 };
      vehicle.createdBy = createdBy;
      const updatedBy: IUser = { id: 86548 };
      vehicle.updatedBy = updatedBy;
      const approvedBy: IUser = { id: 66738 };
      vehicle.approvedBy = approvedBy;
      const assignedDriver: IEmployee = { id: 89174 };
      vehicle.assignedDriver = assignedDriver;

      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(approvedBy);
      expect(comp.employeesSharedCollection).toContain(assignedDriver);
      expect(comp.vehicle).toEqual(vehicle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicle>>();
      const vehicle = { id: 123 };
      jest.spyOn(vehicleFormService, 'getVehicle').mockReturnValue(vehicle);
      jest.spyOn(vehicleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicle }));
      saveSubject.complete();

      // THEN
      expect(vehicleFormService.getVehicle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleService.update).toHaveBeenCalledWith(expect.objectContaining(vehicle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicle>>();
      const vehicle = { id: 123 };
      jest.spyOn(vehicleFormService, 'getVehicle').mockReturnValue({ id: null });
      jest.spyOn(vehicleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicle }));
      saveSubject.complete();

      // THEN
      expect(vehicleFormService.getVehicle).toHaveBeenCalled();
      expect(vehicleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicle>>();
      const vehicle = { id: 123 };
      jest.spyOn(vehicleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleService.update).toHaveBeenCalled();
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

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
