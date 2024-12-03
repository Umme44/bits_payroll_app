import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeePinConfigurationFormService } from './employee-pin-configuration-form.service';
import { EmployeePinConfigurationService } from '../service/employee-pin-configuration.service';
import { IEmployeePinConfiguration } from '../employee-pin-configuration.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { EmployeePinConfigurationUpdateComponent } from './employee-pin-configuration-update.component';

describe('EmployeePinConfiguration Management Update Component', () => {
  let comp: EmployeePinConfigurationUpdateComponent;
  let fixture: ComponentFixture<EmployeePinConfigurationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeePinConfigurationFormService: EmployeePinConfigurationFormService;
  let employeePinConfigurationService: EmployeePinConfigurationService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeePinConfigurationUpdateComponent],
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
      .overrideTemplate(EmployeePinConfigurationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeePinConfigurationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeePinConfigurationFormService = TestBed.inject(EmployeePinConfigurationFormService);
    employeePinConfigurationService = TestBed.inject(EmployeePinConfigurationService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const employeePinConfiguration: IEmployeePinConfiguration = { id: 456 };
      const createdBy: IUser = { id: 97592 };
      employeePinConfiguration.createdBy = createdBy;
      const updatedBy: IUser = { id: 29205 };
      employeePinConfiguration.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 29830 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeePinConfiguration });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeePinConfiguration: IEmployeePinConfiguration = { id: 456 };
      const createdBy: IUser = { id: 68239 };
      employeePinConfiguration.createdBy = createdBy;
      const updatedBy: IUser = { id: 88552 };
      employeePinConfiguration.updatedBy = updatedBy;

      activatedRoute.data = of({ employeePinConfiguration });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.employeePinConfiguration).toEqual(employeePinConfiguration);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeePinConfiguration>>();
      const employeePinConfiguration = { id: 123 };
      jest.spyOn(employeePinConfigurationFormService, 'getEmployeePinConfiguration').mockReturnValue(employeePinConfiguration);
      jest.spyOn(employeePinConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeePinConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeePinConfiguration }));
      saveSubject.complete();

      // THEN
      expect(employeePinConfigurationFormService.getEmployeePinConfiguration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeePinConfigurationService.update).toHaveBeenCalledWith(expect.objectContaining(employeePinConfiguration));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeePinConfiguration>>();
      const employeePinConfiguration = { id: 123 };
      jest.spyOn(employeePinConfigurationFormService, 'getEmployeePinConfiguration').mockReturnValue({ id: null });
      jest.spyOn(employeePinConfigurationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeePinConfiguration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeePinConfiguration }));
      saveSubject.complete();

      // THEN
      expect(employeePinConfigurationFormService.getEmployeePinConfiguration).toHaveBeenCalled();
      expect(employeePinConfigurationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeePinConfiguration>>();
      const employeePinConfiguration = { id: 123 };
      jest.spyOn(employeePinConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeePinConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeePinConfigurationService.update).toHaveBeenCalled();
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
