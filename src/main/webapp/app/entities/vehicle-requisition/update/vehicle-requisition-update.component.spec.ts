import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VehicleRequisitionFormService } from './vehicle-requisition-form.service';
import { VehicleRequisitionService } from '../service/vehicle-requisition.service';
import { IVehicleRequisition } from '../vehicle-requisition.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/vehicle/service/vehicle.service';

import { VehicleRequisitionUpdateComponent } from './vehicle-requisition-update.component';

describe('VehicleRequisition Management Update Component', () => {
  let comp: VehicleRequisitionUpdateComponent;
  let fixture: ComponentFixture<VehicleRequisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleRequisitionFormService: VehicleRequisitionFormService;
  let vehicleRequisitionService: VehicleRequisitionService;
  let userService: UserService;
  let employeeService: EmployeeService;
  let vehicleService: VehicleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VehicleRequisitionUpdateComponent],
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
      .overrideTemplate(VehicleRequisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleRequisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleRequisitionFormService = TestBed.inject(VehicleRequisitionFormService);
    vehicleRequisitionService = TestBed.inject(VehicleRequisitionService);
    userService = TestBed.inject(UserService);
    employeeService = TestBed.inject(EmployeeService);
    vehicleService = TestBed.inject(VehicleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const vehicleRequisition: IVehicleRequisition = { id: 456 };
      const createdBy: IUser = { id: 21639 };
      vehicleRequisition.createdBy = createdBy;
      const updatedBy: IUser = { id: 43052 };
      vehicleRequisition.updatedBy = updatedBy;
      const approvedBy: IUser = { id: 18773 };
      vehicleRequisition.approvedBy = approvedBy;

      const userCollection: IUser[] = [{ id: 57572 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy, approvedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicleRequisition });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const vehicleRequisition: IVehicleRequisition = { id: 456 };
      const requester: IEmployee = { id: 69479 };
      vehicleRequisition.requester = requester;

      const employeeCollection: IEmployee[] = [{ id: 27144 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [requester];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicleRequisition });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vehicle query and add missing value', () => {
      const vehicleRequisition: IVehicleRequisition = { id: 456 };
      const vehicle: IVehicle = { id: 43688 };
      vehicleRequisition.vehicle = vehicle;

      const vehicleCollection: IVehicle[] = [{ id: 870 }];
      jest.spyOn(vehicleService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleCollection })));
      const additionalVehicles = [vehicle];
      const expectedCollection: IVehicle[] = [...additionalVehicles, ...vehicleCollection];
      jest.spyOn(vehicleService, 'addVehicleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicleRequisition });
      comp.ngOnInit();

      expect(vehicleService.query).toHaveBeenCalled();
      expect(vehicleService.addVehicleToCollectionIfMissing).toHaveBeenCalledWith(
        vehicleCollection,
        ...additionalVehicles.map(expect.objectContaining)
      );
      expect(comp.vehiclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vehicleRequisition: IVehicleRequisition = { id: 456 };
      const createdBy: IUser = { id: 76094 };
      vehicleRequisition.createdBy = createdBy;
      const updatedBy: IUser = { id: 14940 };
      vehicleRequisition.updatedBy = updatedBy;
      const approvedBy: IUser = { id: 73164 };
      vehicleRequisition.approvedBy = approvedBy;
      const requester: IEmployee = { id: 64987 };
      vehicleRequisition.requester = requester;
      const vehicle: IVehicle = { id: 25753 };
      vehicleRequisition.vehicle = vehicle;

      activatedRoute.data = of({ vehicleRequisition });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(approvedBy);
      expect(comp.employeesSharedCollection).toContain(requester);
      expect(comp.vehiclesSharedCollection).toContain(vehicle);
      expect(comp.vehicleRequisition).toEqual(vehicleRequisition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleRequisition>>();
      const vehicleRequisition = { id: 123 };
      jest.spyOn(vehicleRequisitionFormService, 'getVehicleRequisition').mockReturnValue(vehicleRequisition);
      jest.spyOn(vehicleRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleRequisition }));
      saveSubject.complete();

      // THEN
      expect(vehicleRequisitionFormService.getVehicleRequisition).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleRequisitionService.update).toHaveBeenCalledWith(expect.objectContaining(vehicleRequisition));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleRequisition>>();
      const vehicleRequisition = { id: 123 };
      jest.spyOn(vehicleRequisitionFormService, 'getVehicleRequisition').mockReturnValue({ id: null });
      jest.spyOn(vehicleRequisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleRequisition: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleRequisition }));
      saveSubject.complete();

      // THEN
      expect(vehicleRequisitionFormService.getVehicleRequisition).toHaveBeenCalled();
      expect(vehicleRequisitionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicleRequisition>>();
      const vehicleRequisition = { id: 123 };
      jest.spyOn(vehicleRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleRequisitionService.update).toHaveBeenCalled();
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

    describe('compareVehicle', () => {
      it('Should forward to vehicleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vehicleService, 'compareVehicle');
        comp.compareVehicle(entity, entity2);
        expect(vehicleService.compareVehicle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
