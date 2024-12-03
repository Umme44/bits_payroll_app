import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReferencesDetailComponent } from './references-detail.component';

describe('References Management Detail Component', () => {
  let comp: ReferencesDetailComponent;
  let fixture: ComponentFixture<ReferencesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReferencesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ references: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReferencesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReferencesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load references on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.references).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
