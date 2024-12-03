import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MovementEntryFormService } from './movement-entry-form.service';
import { MovementEntryService } from '../service/movement-entry.service';
import { IMovementEntry } from '../movement-entry.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { MovementEntryUpdateComponent } from './movement-entry-update.component';

describe('MovementEntry Management Update Component', () => {
  let comp: MovementEntryUpdateComponent;
  let fixture: ComponentFixture<MovementEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let movementEntryFormService: MovementEntryFormService;
  let movementEntryService: MovementEntryService;
  let employeeService: EmployeeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MovementEntryUpdateComponent],
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
      .overrideTemplate(MovementEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MovementEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    movementEntryFormService = TestBed.inject(MovementEntryFormService);
    movementEntryService = TestBed.inject(MovementEntryService);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const movementEntry: IMovementEntry = { id: 456 };
      const employee: IEmployee = { id: 36887 };
      movementEntry.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 92112 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ movementEntry });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const movementEntry: IMovementEntry = { id: 456 };
      const createdBy: IUser = { id: 69361 };
      movementEntry.createdByLogin = createdBy;
      const updatedBy: IUser = { id: 95269 };
      movementEntry.updatedByLogin = updatedBy;
      const sanctionBy: IUser = { id: 48193 };
      movementEntry.sanctionByLogin = sanctionBy;

      const userCollection: IUser[] = [{ id: 17197 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy, sanctionBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ movementEntry });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const movementEntry: IMovementEntry = { id: 456 };
      const employee: IEmployee = { id: 25816 };
      movementEntry.employee = employee;
      const createdBy: IUser = { id: 84252 };
      movementEntry.createdByLogin = createdBy;
      const updatedBy: IUser = { id: 97535 };
      movementEntry.updatedByLogin = updatedBy;
      const sanctionBy: IUser = { id: 2543 };
      movementEntry.sanctionByLogin = sanctionBy;

      activatedRoute.data = of({ movementEntry });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(sanctionBy);
      expect(comp.movementEntry).toEqual(movementEntry);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMovementEntry>>();
      const movementEntry = { id: 123 };
      jest.spyOn(movementEntryFormService, 'getMovementEntry').mockReturnValue(movementEntry);
      jest.spyOn(movementEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movementEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movementEntry }));
      saveSubject.complete();

      // THEN
      expect(movementEntryFormService.getMovementEntry).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(movementEntryService.update).toHaveBeenCalledWith(expect.objectContaining(movementEntry));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMovementEntry>>();
      const movementEntry = { id: 123 };
      jest.spyOn(movementEntryFormService, 'getMovementEntry').mockReturnValue({ id: null });
      jest.spyOn(movementEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movementEntry: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movementEntry }));
      saveSubject.complete();

      // THEN
      expect(movementEntryFormService.getMovementEntry).toHaveBeenCalled();
      expect(movementEntryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMovementEntry>>();
      const movementEntry = { id: 123 };
      jest.spyOn(movementEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movementEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(movementEntryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
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
