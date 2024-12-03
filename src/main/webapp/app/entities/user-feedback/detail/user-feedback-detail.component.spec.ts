import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserFeedbackDetailComponent } from './user-feedback-detail.component';

describe('UserFeedback Management Detail Component', () => {
  let comp: UserFeedbackDetailComponent;
  let fixture: ComponentFixture<UserFeedbackDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserFeedbackDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ userFeedback: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UserFeedbackDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UserFeedbackDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userFeedback on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.userFeedback).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
