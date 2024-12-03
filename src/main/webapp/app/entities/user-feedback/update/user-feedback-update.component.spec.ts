import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserFeedbackFormService } from './user-feedback-form.service';
import { UserFeedbackService } from '../service/user-feedback.service';
import { IUserFeedback } from '../user-feedback.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UserFeedbackUpdateComponent } from './user-feedback-update.component';

describe('UserFeedback Management Update Component', () => {
  let comp: UserFeedbackUpdateComponent;
  let fixture: ComponentFixture<UserFeedbackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userFeedbackFormService: UserFeedbackFormService;
  let userFeedbackService: UserFeedbackService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserFeedbackUpdateComponent],
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
      .overrideTemplate(UserFeedbackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserFeedbackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userFeedbackFormService = TestBed.inject(UserFeedbackFormService);
    userFeedbackService = TestBed.inject(UserFeedbackService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userFeedback: IUserFeedback = { id: 456 };
      const user: IUser = { id: 63502 };
      userFeedback.user = user;

      const userCollection: IUser[] = [{ id: 38029 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userFeedback });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userFeedback: IUserFeedback = { id: 456 };
      const user: IUser = { id: 46882 };
      userFeedback.user = user;

      activatedRoute.data = of({ userFeedback });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.userFeedback).toEqual(userFeedback);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserFeedback>>();
      const userFeedback = { id: 123 };
      jest.spyOn(userFeedbackFormService, 'getUserFeedback').mockReturnValue(userFeedback);
      jest.spyOn(userFeedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userFeedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userFeedback }));
      saveSubject.complete();

      // THEN
      expect(userFeedbackFormService.getUserFeedback).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userFeedbackService.update).toHaveBeenCalledWith(expect.objectContaining(userFeedback));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserFeedback>>();
      const userFeedback = { id: 123 };
      jest.spyOn(userFeedbackFormService, 'getUserFeedback').mockReturnValue({ id: null });
      jest.spyOn(userFeedbackService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userFeedback: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userFeedback }));
      saveSubject.complete();

      // THEN
      expect(userFeedbackFormService.getUserFeedback).toHaveBeenCalled();
      expect(userFeedbackService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserFeedback>>();
      const userFeedback = { id: 123 };
      jest.spyOn(userFeedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userFeedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userFeedbackService.update).toHaveBeenCalled();
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
